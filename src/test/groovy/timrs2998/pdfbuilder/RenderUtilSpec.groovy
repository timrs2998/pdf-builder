package timrs2998.pdfbuilder

import spock.lang.Specification
import spock.lang.Unroll

class RenderUtilSpec extends Specification {

    @Unroll
    void 'gets index of page #y / #pageHeight'(float pageHeight, float y, int expected) {
        when:
        int response = RenderUtilKt.getPageIndex(pageHeight, y)

        then:
        response == expected

        where:
        pageHeight | y    | expected
        100f       | 0f   | 0
        100f       | 99f  | 0
        100f       | 100f | 1
        100f       | 101f | 1
        100f       | 199f | 1
        100f       | 200f | 2
        100f       | 201f | 2
    }

}
