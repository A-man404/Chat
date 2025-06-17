package com.example.plugins

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail


fun emailSender(subject:String,sendTo:String,message:String) {
    val email = SimpleEmail()
    email.hostName = "smtp.googlemail.com"
    email.setSmtpPort(465)
    email.setAuthenticator(DefaultAuthenticator("neoconnect53@gmail.com", "qpvc xkgw izse cdwu"))
    email.isSSLOnConnect = true
    email.setFrom("dollaraman0@gmail.com")
    email.subject = subject
    email.setMsg(message)
    email.addTo(sendTo)
    email.send()
}
