package com.github

import java.io._

import com.github.tototoshi.csv.{CSVFormat, DefaultCSVFormat, CSVReader => CReader}

import scala.util.{Failure, Success, Try}

/**
  * Created by zhoudunxiong on 2018/4/11.
  */
package object scalacsv {
  import Using._

  implicit class CSVIterable[A](iter: Iterable[A])(implicit enc: Encoder[A]) {

    val format = new DefaultCSVFormat {}

    def asCSVLines(sep: Char = format.delimiter, header: Seq[String]): Iterable[String] = {
      val h = if (header.isEmpty) header else Iterable(header.mkString(sep.toString))
      h ++ iter.map(a => enc.encode(a).mkString(sep.toString))
    }

    def asCSV(sep: Char = format.delimiter, header: Seq[String] = Nil): String =
      asCSVLines(sep, header).mkString(System.lineSeparator())

    def writeCSV(writer: PrintWriter, sep: Char, header: Seq[String] = Nil): Unit =
      asCSVLines(sep, header).foreach(writer.println)

    def writeCSVToFile(file: File, sep: Char = format.delimiter, header: Seq[String] = Nil): Unit =
      using(new PrintWriter(file))(writeCSV(_, sep, header))

    def writeCSVToFileName(filePath: String, sep: Char = format.delimiter, header: Seq[String] = Nil): Unit =
      writeCSVToFile(new File(filePath), sep, header)
  }

  trait CSVReader[A] {
    val dec: Decoder[A]

    def getRecords(reader: Reader, skipHeader: Boolean, format: CSVFormat): Iterable[Seq[String]] = {
      val emptyLine = (l: Seq[String]) => l.isEmpty || l.head.isEmpty
      val records = CReader.open(reader)(format).all().filterNot(emptyLine)
      if(skipHeader)
        records.drop(1)
      else
        records
    }

    def readCSV(reader: Reader, skipHeader: Boolean = false)(implicit format: CSVFormat): Iterable[Try[A]] = {
      val records = getRecords(reader, skipHeader, format)
      records.map(dec.decode)
    }

    def readCSVFromFile(file: File, encoding: String = "utf-8",
                        skipHeader: Boolean = false)(implicit format: CSVFormat): Iterable[Try[A]] =
      using(new InputStreamReader(new FileInputStream(file), encoding)){r =>
        readCSV(r, skipHeader)
      }


    def readCSVFromFileName(fileName: String, encoding: String = "utf-8",
                            skipHeader: Boolean = false)(implicit format: CSVFormat): Iterable[Try[A]] =
      readCSVFromFile(new File(fileName), encoding, skipHeader)

    def readCSVFromString(s: String, skipHeader: Boolean = false)(implicit format: CSVFormat): Iterable[Try[A]] =
      using(new StringReader(s)){r =>
        readCSV(r, skipHeader)
      }
  }

  object CSVReader {
    def apply[A](implicit decoder: Decoder[A]): CSVReader[A] = new CSVReader[A] {
      override val dec: Decoder[A] = decoder
    }
  }

  implicit class TryIterable[A](iter: Iterable[Try[A]]) {

    def sequence: Try[Iterable[A]] = {
      if (iter.exists(_.isFailure)) {
        val s = iter.zipWithIndex.filter{case (f, _) => f.isFailure}
          .map{case (f, i) => (f.failed.get.getMessage, i)}
          .foldLeft(""){(b, a) =>
            val (message, i) = a
            b + "\n" + s"index: $i, $message"
          }
        Failure(new Exception(s))
      } else Success(iter.map(_.get))
    }
  }

}
