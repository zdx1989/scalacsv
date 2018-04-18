import com.github.scalacsv.Encoder
import org.scalatest.{FunSpec, Matchers}
import shapeless.{HNil, ::}

/**
  * Created by zhoudunxiong on 2018/4/14.
  */
class EncoderSpec extends FunSpec with Matchers {

  describe("encoder") {
    it("should encode Boolean -> List[String]") {
      Encoder[Boolean].encode(true) should be (List("true"))
      Encoder[Boolean].encode(false) should be (List("false"))
    }

    it("should encode Short -> List[String]") {
      Encoder[Short].encode(1) should be (List("1"))
    }

    it("should encode Long -> List[String]") {
      Encoder[Long].encode(1) should be (List("1"))
    }

    it("should encode Int -> List[String]") {
      Encoder[Int].encode(1) should be (List("1"))
    }

    it("should encode Float -> List[String]") {
      Encoder[Float].encode(1.0F) should be (List("1.0"))
    }

    it("should encoder Double -> List[String]") {
      Encoder[Double].encode(1.0) should be (List("1.0"))
    }

    it("should encoder Char -> List[String]") {
      Encoder[Char].encode('a') should be (List("a"))
    }

    it("should encoder Byte -> List[String]") {
      Encoder[Byte].encode(1) should be (List("1"))
    }

    it("should encoder String -> List[String]") {
      Encoder[String].encode("abc") should be (List("abc"))
    }

    it("should encode Option[Int] -> List[String]") {
      Encoder[Option[Int]].encode(Some(1)) should be (List("1"))
      Encoder[Option[Int]].encode(None) should be (List.empty)
    }

    it("should encode HNil -> List[String]") {
      Encoder[HNil].encode(HNil) should be (List.empty)
    }

    it("should encoder HList -> List[String]") {
      Encoder[Int :: String :: HNil].encode(1 :: "zdx" :: HNil) should be (List("1", "zdx"))
    }

    it("should encoder case class -> List[String]") {
      case class Student(id: Int, name: String)
      Encoder[Student].encode(Student(1, "zdx")) should be (List("1", "zdx"))
      case class Id(id: Int)
      Encoder[Id].encode(Id(1)) should be (List("1"))
    }
  }

}
