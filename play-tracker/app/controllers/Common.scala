package controllers

import play.api.data.Form
import play.api.data.Forms.mapping

import java.text.SimpleDateFormat
import java.util.Date

import javax.inject._
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.Lang
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._
import play.filters.csrf._
import play.filters.headers.SecurityHeadersFilter

trait Common {


  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "userpassword" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  )

  val ticketForm = Form(
    mapping(
      "ticketName" -> nonEmptyText,
      "ticketDetails" -> nonEmptyText
    )(TicketData.apply)(TicketData.unapply)
  )

}
