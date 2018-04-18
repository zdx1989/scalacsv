import com.github.scalacsv.Decoder
import org.scalatest.{FunSpec, Matchers}
import shapeless.{HNil, ::}

import scala.util.Success

/**
  * Created by zhoudunxiong on 2018/4/13.
  */
class DecoderSpec extends FunSpec with Matchers {

  describe("Decoder") {
    it("should decode List[String] -> Try[Boolean]") {
      Decoder[Boolean].decode(List("true")) should be (Success(true))
      Decoder[Boolean].decode(List("false")) should be (Success(false))
      an [IllegalArgumentException] should be thrownBy Decoder[Boolean].decode(List("Boolean")).get
    }

    it("should decode List[String] -> Try[Short]") {
      Decoder[Short].decode(List("1")) should be (Success(1))
      an [IllegalArgumentException] should be thrownBy Decoder[Short].decode(List("Short")).get
    }

    it("should decode List[String] -> Try[Long]") {
      Decoder[Long].decode(List("1")) should be (Success(1L))
      an [IllegalArgumentException] should be thrownBy Decoder[Long].decode(List("Long")).get
    }

    it("should decode List[String] -> Try[Int]") {
      Decoder[Int].decode(List("1")) should be (Success(1))
      an [IllegalArgumentException] should be thrownBy Decoder[Int].decode(List("Int")).get
    }

    it("should decode List[String] -> Try[Float]") {
      Decoder[Float].decode(List("1.0")) should be (Success(1.0))
      an [IllegalArgumentException] should be thrownBy Decoder[Float].decode(List("Float")).get
    }

    it("should decode List[String] -> Try[Double]") {
      Decoder[Double].decode(List("1.0")) should be (Success(1.0))
      an [IllegalArgumentException] should be thrownBy Decoder[Double].decode(List("Double")).get
    }

    it("should decode List[String] -> Try[Char]") {
      Decoder[Char].decode(List("a")) should be (Success('a'))
      an [IllegalArgumentException] should be thrownBy Decoder[Char].decode(List("abc")).get
    }

    it("should decode List[String] -> Try[Byte]") {
      Decoder[Byte].decode(List("1")) should be (Success(1))
      an [IllegalArgumentException] should be thrownBy Decoder[Byte].decode(List("Byte")).get
    }

    it("should decode List[String] -> Try[String]") {
      Decoder[String].decode(List("abc")) should be (Success("abc"))
      Decoder[String].decode(List("123")) should be (Success("123"))
    }

    it("should decode List[String] -> Try[Option[Int]]") {
      Decoder[Option[Int]].decode(List("1")) should be (Success(Some(1)))
      Decoder[Option[Int]].decode(List.empty) should be (Success(None))
    }

    it("should decode List[String] -> HNil") {
      Decoder[HNil].decode(List.empty) should be (Success(HNil))
    }

    it("should decode List[String] -> HList") {
      Decoder[Int :: String :: HNil].decode(List("1", "zdx")) should be (Success(1 :: "zdx" :: HNil))
    }

    it("should decode List[String] -> case class") {
      case class Student(id: Int, name: String)
      Decoder[Student].decode(List("1", "zdx")) should be (Success(Student(1, "zdx")))
      case class Id(id: Int)
      Decoder[Id].decode(List("1")) should be (Success(Id(1)))
    }
  }

}
