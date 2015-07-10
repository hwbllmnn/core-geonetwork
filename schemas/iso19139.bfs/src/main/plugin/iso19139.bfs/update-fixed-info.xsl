<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:bfs="http://geonetwork.org/bfs" version="2.0"
                exclude-result-prefixes="#all">

    <xsl:import href="../iso19139/update-fixed-info.xsl"/>

    <xsl:template match="bfs:MD_Metadata">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>

            <gmd:fileIdentifier>
                <gco:CharacterString>
                    <xsl:value-of select="/root/env/uuid"/>
                </gco:CharacterString>
            </gmd:fileIdentifier>

            <xsl:apply-templates select="gmd:language"/>
            <xsl:apply-templates select="gmd:characterSet"/>

            <xsl:choose>
                <xsl:when test="/root/env/parentUuid!=''">
                    <gmd:parentIdentifier>
                        <gco:CharacterString>
                            <xsl:value-of select="/root/env/parentUuid"/>
                        </gco:CharacterString>
                    </gmd:parentIdentifier>
                </xsl:when>
                <xsl:when test="gmd:parentIdentifier">
                    <xsl:copy-of select="gmd:parentIdentifier"/>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="node()[not(self::gmd:language) and not(self::gmd:characterSet)]"/>
        </xsl:copy>
    </xsl:template>


    <!-- ================================================================= -->
    <!-- Do not process MD_Metadata header generated by previous template  -->

    <xsl:template match="bfs:MD_Metadata/gmd:fileIdentifier|bfs:MD_Metadata/gmd:parentIdentifier" priority="10"/>
</xsl:stylesheet>