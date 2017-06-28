package timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument
import spock.lang.Specification

class TableElementSpec extends Specification {

    void 'create empty table'() {
        given:
        Document document = new Document()

        when:
        TableElement tableElement = new TableElement(document)
        document.children.add(tableElement)

        then:
        0 * _
    }

    void 'render empty table'() {
        given:
        Document document = new Document()
        TableElement tableElement = new TableElement(document)
        document.children.add(tableElement)

        when:
        PDDocument pdDocument = document.render()

        then:
        pdDocument
        0 * _
    }


}
