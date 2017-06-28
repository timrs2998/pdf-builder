package timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument
import spock.lang.Specification

class DocumentSpec extends Specification {

    void 'create empty document'() {
        when:
        Document document = new Document()

        then:
        document.children.empty
        !document.backgroundColor
        !document.fontColor
        !document.fontSize
        !document.horizontalAlignment
        !document.pdFont
        document.inheritedBackgroundColor
        document.inheritedFontColor
        document.inheritedFontSize
        document.inheritedHorizontalAlignment
        document.inheritedPdFont
        0 * _
    }

    void 'render empty document'() {
        given:
        Document document = new Document()

        when:
        PDDocument pdDocument = document.render()

        then:
        pdDocument
        0 * _
    }

}
