package com.github.scalacsv

import scala.util.{Success, Try}

/**
  * Created by zhoudunxiong on 2018/4/9.
  */
trait Decoder[A] {
  def decode(as: Seq[String]): Try[A]
}

object Decoder {
  def apply[A](implicit dec: Decoder[A]): Decoder[A] = dec

  def instance[A](f: Seq[String] => Try[A]): Decoder[A] = new Decoder[A] {
    override def decode(as: Seq[String]): Try[A] = f(as)
  }

  def trySeq[A](f: String => A): Seq[String] => Try[A] = as => Try(f(as.head))

  def str2Char(s: String): Char =
    if (s.length == 1) s.head
    else throw new IllegalArgumentException(s"$s can not convert to char")

  implicit val intDec: Decoder[Int] = instance(trySeq(_.toInt))
  implicit val shortDec: Decoder[Short] = instance(trySeq(_.toShort))
  implicit val longDec: Decoder[Long] = instance(trySeq(_.toLong))
  implicit val floatDec: Decoder[Float] = instance(trySeq(_.toFloat))
  implicit val doubleDec: Decoder[Double] = instance(trySeq(_.toDouble))
  implicit val booleanDec: Decoder[Boolean] = instance(trySeq(_.toBoolean))
  implicit val charDec: Decoder[Char] = instance(trySeq(str2Char))
  implicit val byteDec: Decoder[Byte] = instance(trySeq(_.toByte))
  implicit val stringDec: Decoder[String] = instance(trySeq(_.toString))

  implicit def optionDec[A](implicit dec: Decoder[A]): Decoder[Option[A]] =
    instance {
      case Nil => Success(None)
      case as: Seq[String] => dec.decode(as).map(Some(_))
    }

  import shapeless.{HNil, ::, HList, Generic}

  implicit val hNilDec: Decoder[HNil] = instance(as => Try(HNil))

  implicit def hListDec[H, T <: HList](implicit hDec: Decoder[H], tDec: Decoder[T]): Decoder[H :: T] =
    instance{
      case head +: tail => for {
        h <- hDec.decode(Seq(head))
        t <- tDec.decode(tail)
      } yield h :: t
    }

  implicit def generic[A, R](implicit gen: Generic.Aux[A, R], rDec: Decoder[R]): Decoder[A] =
    instance{ as =>
      rDec.decode(as).map(gen.from)
    }
}
