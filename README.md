# scala-csv #

scala-csv是一个由Scala编写的读写CSV库


### 读取CSV字符串 ###

```scala
scala> case class Student(id: Int, name: String)
defined class Student
scala> import com.github.scalacsv._
import com.github.scalacsv._
scala> CSVReader[Student].readCSVFromString("1,zdx\n2,ygy")
res5: Iterable[scala.util.Try[Student]] = List(Success(Student(1,zdx)), Success(Student(2,ygy)))
scala> CSVReader[(Int, String)].readCSVFromString("1,zdx\n2,ygy")
res6: Iterable[scala.util.Try[(Int, String)]] = List(Success((1,zdx)), Success((2,ygy)))
```

### 读取CSV文件 ###

```scala
scala> case class Student(id: Int, name: String)
defined class Student
scala> import com.github.scalacsv._
import com.github.scalacsv._
scala> import java.io.File
import java.io.File
scala> CSVReader[(Int, String)].readCSVFromFile(new File("/tmp/simple.csv"))
res9: Iterable[scala.util.Try[(Int, String)]] = List(Success((1,zdx)), Success((2,ygy)))
scala> CSVReader[Student].readCSVFromFileName("/tmp/simple.csv")
res11: Iterable[scala.util.Try[Student]] = List(Success(Student(1,zdx)), Success(Student(2,ygy)))
scala> CSVReader[Student].readCSVFromFileName("/tmp/simple.csv").sequence
res12: scala.util.Try[Iterable[Student]] = Success(List(Student(1,zdx), Student(2,ygy)))
```


### 写CSV字符串 ###

```scala
scala> case class Student(id: Int, name: String)
defined class Student
scala> import com.github.scalacsv._
import com.github.scalacsv._
scala> List(Student(1, "zdx"), Student(2, "ygy")).asCSV()
res14: String =
1,zdx
2,ygy
scala> List((1, "zdx"), (2, "ygy")).asCSV('#')
res16: String =
1#zdx
2#ygy
```

### 写CSV文件 ###

```scala
scala> case class Student(id: Int, name: String)
defined class Student
scala> import com.github.scalacsv._
scala> import java.io.File
import java.io.File
import com.github.scalacsv._
scala> List((1, "zdx"), (2, "ygy")).writeCSVToFile(new File("/tmp/simple.csv"))
scala> scala.io.Source.fromFile("/tmp/simple.csv").getLines.mkString("\n")
res22: String =
1,zdx
2,ygy
scala> List(Student(1, "zdx"), Student(2, "ygy")).writeCSVToFileName("/tmp/simple.csv", header = Seq("id", "name"))
scala> scala.io.Source.fromFile("/tmp/simple.csv").getLines.mkString("\n")
res24: String =
id,name
1,zdx
2,ygy

```

### 特别鸣谢 ###
[Shapeless](https://github.com/milessabin/shapeless)
[scala-csv](https://github.com/tototoshi/scala-csv)
[PureCSV](https://github.com/melrief/PureCSV)
