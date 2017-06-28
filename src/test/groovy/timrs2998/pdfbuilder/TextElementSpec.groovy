package timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument
import spock.lang.Specification
import spock.lang.Unroll

class TextElementSpec extends Specification {

    void 'render text element'() {
        given:
        Document document = new Document()
        TextElement textElement = new TextElement(document, 'Hello, world!')
        document.children.add(textElement)

        when:
        PDDocument pdDocument = document.render()

        then:
        pdDocument
        0 * _
    }

    @Unroll
    void 'wraps text on word boundaries with width #width'(float width, List<String> expected) {
        given:
        TextElement textElement = new TextElement(new Document(), 'Hello, world!')

        when:
        List<String> response = textElement.wrapText(width)

        then:
        response == expected

        where:
        width   | expected
        100f    | ['Hello, world!']
        50f     | ['Hello,', 'world!']
    }

}
