<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="strong|b">
    <xsl:text>**</xsl:text>
    <xsl:apply-templates select="* | text()"/>
    <xsl:text>**</xsl:text>
  </xsl:template>

  <xsl:template match="em|i">
    <xsl:text>_</xsl:text>
    <xsl:apply-templates select="* | text()"/>
    <xsl:text>_</xsl:text>
  </xsl:template>

  <xsl:template match="br">
    <xsl:text>&#xA;&#xA;</xsl:text>
  </xsl:template>

  <!-- Pre-formatted blocks are not supported by the current MD subset,
       so treat them like inline ones. -->
  <xsl:template match="code">
    <xsl:text>`</xsl:text>
    <xsl:value-of select="text()"/>
    <xsl:text>`</xsl:text>
  </xsl:template>

  <xsl:template match="pre">
    <xsl:text>`</xsl:text>
    <xsl:apply-templates select="text()"/>
    <xsl:text>`</xsl:text>
  </xsl:template>

  <xsl:template match="text()[position() = 1 or
                            ((preceding-sibling::*)[last()][self::div        or
                                                            self::p          or
                                                            self::pre        or
                                                            self::br         ])]">
    <xsl:variable name="text-w-spaces"
                  select="translate(., '&#xA;&#xD;&#x9;&#x20;', '&#x20;&#x20;&#x20;&#x20;')"/>
    <xsl:choose>
      <xsl:when test="translate(., '&#xA;&#xD;&#x9;&#x20;', '') = ''">
        <xsl:text></xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:variable name="trailing-char">
          <xsl:if test="substring($text-w-spaces, string-length($text-w-spaces), 1) = '&#x20;'">
            <xsl:text>&#x20;</xsl:text>
          </xsl:if>
        </xsl:variable>
        <xsl:value-of select="concat(normalize-space($text-w-spaces), $trailing-char)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Keep others. -->
  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>