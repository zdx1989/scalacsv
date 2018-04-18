package com.github.scalacsv

/**
  * Created by zhoudunxiong on 2018/4/12.
  */
trait Using {

  def using[A, B <: {def close(): Unit}](b: B)(f: B => A): A =
    try f(b) finally b.close()
}

object Using extends Using
