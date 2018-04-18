import java.io.File

import org.scalatest.{FunSpec, Matchers}

import scala.util.Success
import com.github.scalacsv._
import com.github.tototoshi.csv.DefaultCSVFormat

/**
  * Created by zhoudunxiong on 2018/4/14.
  */
class CSVReaderSpec extends FunSpec with Matchers {

  val path = "./src/test/resources"

  case class Student(id: Int, name: String)

  describe("CSVReader") {
    it("should read csv from java.io.File") {
      val students1 = CSVReader[(Int, String)].readCSVFromFile(new File(s"$path/simple.csv"))
      students1 should be (List(
        Success((1, "zdx")),
        Success((2, "ygy"))))
      val students2 = CSVReader[Student].readCSVFromFile(new File(s"$path/simple.csv"))
      students2 should be (List(
        Success(Student(1, "zdx")),
        Success(Student(2, "ygy"))))
      val students3 = students2.sequence
      students3 should be (Success(List(
        Student(1, "zdx"),
        Student(2, "ygy"))))
    }

    it("should read csv from filePath") {
      val students = CSVReader[Student].readCSVFromFileName(s"$path/simple.csv")
      students should be (List(
        Success(Student(1, "zdx")),
        Success(Student(2, "ygy"))))
    }

    it("should read csv from string") {
      val students = CSVReader[Student].readCSVFromString("1,zdx\n2,ygy")
      students should be (List(
        Success(Student(1, "zdx")),
        Success(Student(2, "ygy"))))
    }

    it("should read csv with CSVFormat") {
      implicit object format extends DefaultCSVFormat {
        override val delimiter: Char = '#'
      }
      val students = CSVReader[Student].readCSVFromFileName(s"$path/hash-separated.csv")
      students should be (List(
        Success(Student(1, "周大侠")),
        Success(Student(2, "银古月"))))
    }

    it("should read csv with header") {
      val students = CSVReader[Student].readCSVFromFileName(s"$path/simple-header.csv", skipHeader = true)
      students should be (List(
        Success(Student(1, "zdx")),
        Success(Student(2, "ygy"))))
    }

    it("should read csv without empty line") {
      implicit val format = new DefaultCSVFormat {
        override val treatEmptyLineAsNil: Boolean = true
      }
      val students = CSVReader[Student].readCSVFromFileName(s"$path/simple-emptyline.csv")
      students should be (List(
        Success(Student(1, "zdx")),
        Success(Student(2, "ygy"))))
    }

    it("should read csv whose escape char is backslash") {
      implicit val format = new DefaultCSVFormat {
        override val escapeChar: Char = '\\'
      }
      val students = CSVReader[Student].readCSVFromFileName(s"$path/backslash-escape.csv")
      students should be (List(
        Success(Student(1, "zdx")),
        Success(Student(2, "ygy is \"zdx\""))))
    }
  }
}
