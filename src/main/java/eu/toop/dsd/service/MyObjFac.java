package eu.toop.dsd.service;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.pd.businesscard.v1.PD1BusinessCardType;
import com.helger.pd.businesscard.v1.PD1BusinessEntityType;
import com.helger.pd.businesscard.v1.PD1ContactType;
import com.helger.pd.businesscard.v1.PD1IdentifierType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

public class MyObjFac {
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.helger.pd.businesscard.v1;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

  @XmlRegistry
  @CodingStyleguideUnaware
  public class ObjectFactory {
    public static final QName _BusinessCard_QNAME = new QName("http://www.peppol.eu/schema/pd/businesscard/20160112/", "BusinessCard");

    public ObjectFactory() {
    }

    @Nonnull
    public PD1BusinessCardType createPD1BusinessCardType() {
      return new PD1BusinessCardType();
    }

    @Nonnull
    public PD1IdentifierType createPD1IdentifierType() {
      return new PD1IdentifierType();
    }

    @Nonnull
    public PD1ContactType createPD1ContactType() {
      return new PD1ContactType();
    }

    @Nonnull
    public PD1BusinessEntityType createPD1BusinessEntityType() {
      return new PD1BusinessEntityType();
    }

    @XmlElementDecl(
        namespace = "http://www.peppol.eu/schema/pd/businesscard/20160112/",
        name = "BusinessCard"
    )
    @Nonnull
    public JAXBElement<PD1BusinessCardType> createBusinessCard(@Nullable PD1BusinessCardType var1) {
      return new JAXBElement(_BusinessCard_QNAME, PD1BusinessCardType.class, (Class)null, var1);
    }
  }
