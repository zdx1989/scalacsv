package com.github.scalacsv

/**
  * Created by zhoudunxiong on 2018/4/9.
  */
trait Encoder[A] {
  def encode(a: A): Seq[String]
}

object Encoder {
  def apply[A](implicit enc: Encoder[A]): Encoder[A] = enc

  def instance[A](f: A => Seq[String]): Encoder[A] = new Encoder[A] {
    override def encode(a: A): Seq[String] = f(a)
  }

  def toSeq[A](f: A => String): A => Seq[String] = a => Seq(f(a))

  implicit val intEnc: Encoder[Int] = instance(toSeq(_.toString))
  implicit val shortEnc: Encoder[Short] = instance(toSeq(_.toString))
  implicit val longEnc: Encoder[Long] = instance(toSeq(_.toString))
  implicit val floatEnc: Encoder[Float] = instance(toSeq(_.toString))
  implicit val doubleEnc: Encoder[Double] = instance(toSeq(_.toString))
  implicit val booleanEnc: Encoder[Boolean] = instance(toSeq(_.toString))
  implicit val charEnc: Encoder[Char] = instance(toSeq(_.toString))
  implicit val byteEnc: Encoder[Byte] = instance(toSeq(_.toString))
  implicit val stringEnc: Encoder[String] = instance(toSeq(_.toString))

  implicit def optionEnc[A](implicit enc: Encoder[A]): Encoder[Option[A]] =
    instance{
      case None => Nil
      case Some(a) => enc.encode(a)
    }

  import shapeless.{HNil, ::, HList, Generic}

  implicit val hNilEnc: Encoder[HNil] = instance(hNil => Nil)

  implicit def hListEnc[H, T <: HList](implicit hEnc: Encoder[H], tEnc: Encoder[T]): Encoder[H :: T] =
    instance{
      case head :: tail => hEnc.encode(head) ++ tEnc.encode(tail)
    }

  implicit def genericEnc[A, R](implicit gen: Generic.Aux[A, R], rEnc: Encoder[R]): Encoder[A] =
    instance{ a =>
      rEnc.encode(gen.to(a))
    }
}
