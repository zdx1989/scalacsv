import java.io.File

import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}

/**
  * Created by zhoudunxiong on 2018/4/17.
  */
class CSVWriterSpec extends FunSpec with Matchers with BeforeAndAfter {

  import com.github.scalacsv._

  import Using._

  def readFileAsString(file: String): String =
    using(io.Source.fromFile(file)) { s =>
      s.getLines().mkString("\n")
    }

  case class Student(id: Int, name: String)

  after {
    new File("test.csv").delete()
  }

  describe("CSVWriter") {

    it("should write Iterable[A] to csv String") {
      val students = List(Student(1, "zdx"), Student(2, "ygy"))
      val csv = students.asCSV()
      csv should be ("1,zdx\n2,ygy")
    }

    it("should write Iterable[A] to csv by file") {
      val students = List(Student(1, "周大侠"), Student(2, "银古月"))
      students.writeCSVToFile(new File("test.csv"))
      readFileAsString("test.csv") should be ("1,周大侠\n2,银古月")
    }

    it("should write Iterable[A] to csv by filePath") {
      val students = Iterable((1, "zdx"), (2, "ygy"))
      students.writeCSVToFileName("test.csv")
      readFileAsString("test.csv") should be ("1,zdx\n2,ygy")
    }

    it("should write Iterable[A] to csv by filePath with header") {
      val students = Iterable((1, "zdx"), (2, "ygy"))
      students.writeCSVToFileName("test.csv", header = Seq("id", "name"))
      readFileAsString("test.csv") should be ("id,name\n1,zdx\n2,ygy")
    }
  }
}
