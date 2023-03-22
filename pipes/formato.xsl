<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="text" indent="yes" encoding="UTF-8"/>
    <xsl:template match="/">
    ¡Hola!

    Se ha producido una nueva subida de artefactos a Nexus. Las URLs de los diferentes artefactos son:

    <xsl:value-of select="notificaciones/text_nexus/@url"/>

    
    El job de Jenkins que ha generado estos artefactos es:

    <xsl:value-of select="notificaciones/text_jenkins/@url"/>

    ¡Muchas gracias y esperamos que este correo haya sido de ayuda!

    
    *Si deseas dejar de recibir estos correos, no olvides añadir NOTIF_NEXUS=false en la sección environment de tu Jenkinsfile ;)        

    </xsl:template>
</xsl:stylesheet>
