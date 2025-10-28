from flask import Flask, jsonify, request
import mysql.connector
from mysql.connector import Error

app = Flask(__name__)

def get_db_connection():
    try:
        connection = mysql.connector.connect(
            host='localhost',
            user='root',
            password='',
            database='micro_habits'
        )
        return connection
    except Error as e:
        print("Error while connecting to MySQL", e)
        return None


@app.route('/get_table/<table>', methods=['GET'])
def get_table(table):
    connection = get_db_connection()
    if connection is None:
        return jsonify({'error': 'Database connection failed'}), 500

    cursor = connection.cursor(dictionary=True)
    cursor.execute(f"SELECT * FROM {table}")
    rows = cursor.fetchall()

    cursor.close()
    connection.close()

    return jsonify(rows)

@app.route('/get_row/<table>', methods=['POST'])
def get_row(table):
    data = request.get_json()
    if data is None:
        return jsonify({'error': 'No data given'}), 400

    fetch_one = data.pop("fetch_one", False)

    id = get_id(table, data, fetch_one)
    if id is None:
        return jsonify({'error': 'Row not found'}), 404

    connection = get_db_connection()
    if connection is None:
        return jsonify({'error': 'Database connection failed'}), 500

    cursor = connection.cursor(dictionary=True)
    if type(id) != tuple:
        cursor.execute(f"SELECT * FROM {table} WHERE id = {id}")
    else:
        cursor.execute(f"SELECT * FROM {table} WHERE id in {id}")

    if fetch_one:
        row = cursor.fetchone()
        cursor.close()
        connection.close()
        return jsonify(row)
    else:
        row = cursor.fetchall()
        cursor.close()
        connection.close()
        return jsonify({"rows": row})

@app.route('/update_row/<table>', methods=['POST'])
def update_row(table):
    data = request.get_json()
    if data is None:
        return jsonify({'error': 'No data given'}), 400

    id = get_id(table, data)

    connection = get_db_connection()
    if connection is None:
        return jsonify({'error': 'Database connection failed'}), 500

    cursor = connection.cursor()

    # add error catching to make sure no errors break the program
    try:
        # If the row exists, update the row with the new values
        if id:
            columns = ', '.join([f"{k} = %s" for k in data.keys()])
            values = tuple(data.values()) + (id,)
            query = f"UPDATE {table} SET {columns} WHERE id = %s"
            cursor.execute(query, values)
            connection.commit()
            cursor.close()
            connection.close()
            return jsonify({'message': 'Row updated', 'id': id}), 200
        # If the row doesn't exist, create a new row with the given values
        else:
            columns = ', '.join(data.keys())
            placeholders = ', '.join(['%s'] * len(data))
            values = tuple(data.values())
            query = f"INSERT INTO {table} ({columns}) VALUES ({placeholders})"
            cursor.execute(query, values)
            connection.commit()
            new_id = cursor.lastrowid
            cursor.close()
            connection.close()
            return jsonify({'message': 'Row created', 'id': new_id}), 201
    except Error as e:
        return jsonify({'error': str(e)}), 400
    except Exception as e:
        return jsonify({'error': 'An unexpected error occurred', 'details': str(e)}), 500


@app.route('/delete_row/<table>', methods=['POST'])
def delete_row(table):
    data = request.get_json()

    if not data:
        return jsonify({'error': 'No data given'}), 400
    
    id = get_id(table, data)
    if id is None:
        return jsonify({'error': 'Row not found'}), 404

    connection = get_db_connection()
    if connection is None:
        return jsonify({'error': 'Database connection failed'}), 500
    
    cursor = connection.cursor()
    cursor.execute(f"DELETE FROM {table} WHERE id = %s", (id, ))
    connection.commit()
    cursor.close()
    connection.close()

    return jsonify({'message': 'Row deleted'}), 200

@app.route('/get_id/<table>', methods=['POST'])
def fetch_id(table):
    data = request.get_json()

    if not data:
        return jsonify({'error': 'No data given'}), 400
    
    id = get_id(table, data)

    if id:
        return jsonify({'id': id}), 200

    return jsonify({'error': 'Row not found'}), 404
    


def get_id(table, data, fetch_one=True):
    if not data:
        return None
    
    if "id" in data:
        id_value = data["id"]
        if isinstance(id_value, (list, tuple)):
            return tuple(id_value)
        else:
            return id_value
        
    connection = get_db_connection()
    if connection is None:
        return None
    
    cursor = connection.cursor()
    columns = list(data.keys())
    placeholders = ' AND '.join([f"{col} = %s" for col in columns])
    values = tuple(data[col] for col in columns)

    query = f"SELECT id FROM {table} WHERE {placeholders}" + (" LIMIT 1" if fetch_one else "")
    cursor.execute(query, values)
    if fetch_one:
        result = cursor.fetchone()
    else:
        result = cursor.fetchall()
    
    cursor.close()
    connection.close()

    if result and fetch_one:
        return result[0]
    if result:
        return tuple(r[0] for r in result)
    
    return None

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)


# http://localhost/phpmyadmin/
# http://localhost:5000/get_table/{table}
