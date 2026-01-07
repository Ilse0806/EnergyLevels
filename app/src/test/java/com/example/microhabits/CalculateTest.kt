package com.example.microhabits

import com.example.microhabits.models.classes.Ingredient
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

// test test file, to test if KoTest is also usable for this project.
class CalculateTest: FunSpec({
    val testIngedrient1 = Ingredient("Test ingredient 1", 400.0, "grams")
    val testIngedrient2 = Ingredient("Test ingredient 1", 2.0, "tbsp")
    val testIngedrient3 = Ingredient("Test ingredient 1", 1.0, "")
    val testIngedrient4 = Ingredient("Test ingredient 1", 3.0, "")

    test("Ingredient: 400.0 with portion 6, should be 600.0") {
        testIngedrient1.calculateAmount(6) shouldBe 600.0
    }
    test("Ingredient: 2.0 with portion 2, should be 1.0") {
        testIngedrient2.calculateAmount(6) shouldBe 1.0
    }
    test("Ingredient: 1.0 with portion 1, should be 0.25") {
        testIngedrient3.calculateAmount(6) shouldBe 0.25
    }
    test("Ingredient: 3.0 with portion 8, should be 6.0") {
        testIngedrient4.calculateAmount(6) shouldBe 6.0
    }
    test("Ingredient: 400.0 with portion 3, should be 300.0") {
        testIngedrient1.calculateAmount(3) shouldBe 300.0
    }
    test("Ingredient: 2.0 with portion 5, should be 2.5") {
        testIngedrient2.calculateAmount(5) shouldBe 2.5
    }
    test("Ingredient: 1.0 with portion 3, should be 0.75") {
        testIngedrient3.calculateAmount(3) shouldBe 0.75
    }
    test("Ingredient: 3.0 with portion 7, should be 5.25") {
        testIngedrient4.calculateAmount(7) shouldBe 5.25
    }
}) {
}
