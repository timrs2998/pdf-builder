package timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument
import spock.lang.Specification

class RowElementSpec extends Specification {

    void 'render empty row'() {
        given:
        Document document = new Document()
        TableElement tableElement = new TableElement(document)
        document.children.add(tableElement)
        RowElement rowElement = new RowElement(tableElement)
        tableElement.rows.add(rowElement)

        when:
        PDDocument pdDocument = document.render()

        then:
        pdDocument
        0 * _
    }

}
