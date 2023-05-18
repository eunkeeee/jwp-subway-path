package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.LineCreateRequest;
import subway.dto.SectionCreateRequest;
import subway.dto.StationCreateRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;
import static subway.steps.LineSteps.*;
import static subway.steps.StationSteps.역_생성하고_아이디_반환;

class LineIntegrationTest extends IntegrationTest {
    public static final LineCreateRequest LINE_NINE_CREATE_REQUEST = new LineCreateRequest("9호선", "BROWN");
    public static final StationCreateRequest EXPRESS_BUS_TERMINAL_REQUEST = new StationCreateRequest(EXPRESS_BUS_TERMINAL_STATION.getName());
    public static final StationCreateRequest SAPYEONG_STATION_REQUEST = new StationCreateRequest(SAPYEONG_STATION.getName());
    public static final StationCreateRequest NEW_STATION_REQUEST = new StationCreateRequest("새 역");

    @Test
    void findAllLinesTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long newStationId = 역_생성하고_아이디_반환(NEW_STATION_REQUEST);

        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        노선에_최초의_역_2개_추가_요청(
                lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));
        존재하는_노선에_역_1개_추가_요청(station1Id, newStationId, lineId);

        final ExtractableResponse<Response> response = 전체_노선_조회_요청();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.body()).isNotNull()
        );
    }

    @Test
    void createNewLineTest() {
        final ExtractableResponse<Response> response = 노선_생성_요청(LINE_NINE_CREATE_REQUEST);

        assertAll(
                () -> assertThat(response.statusCode())
                        .isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location"))
                        .isNotBlank()
        );
    }

    @Test
    void createInitialSectionTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        final ExtractableResponse<Response> response = 노선에_최초의_역_2개_추가_요청(
                lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void createStationInLineTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long newStationId = 역_생성하고_아이디_반환(NEW_STATION_REQUEST);

        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        노선에_최초의_역_2개_추가_요청(lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        final ExtractableResponse<Response> response = 노선에_역_1개_추가_요청(station1Id, newStationId, lineId, 3);

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    private ExtractableResponse<Response> 노선에_역_1개_추가_요청(final Long station1Id, final Long station2Id, final Long lineId, final int distance) {
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                station1Id,
                station2Id,
                distance);

        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)

                .when()
                .post("/lines/" + lineId + "/stations")

                .then()
                .extract();
    }

    @Test
    void deleteStationInLineTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long newStationId = 역_생성하고_아이디_반환(NEW_STATION_REQUEST);

        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        노선에_최초의_역_2개_추가_요청(lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 5
                ));

        노선에_역_1개_추가_요청(station1Id, newStationId, lineId, 3);

        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)

                .when()
                .delete("/lines/" + lineId + "/stations/" + newStationId)

                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @Test
    void addTwoStationsInLineTest() {
        final long station1Id = 역_생성하고_아이디_반환(EXPRESS_BUS_TERMINAL_REQUEST);
        final long station2Id = 역_생성하고_아이디_반환(SAPYEONG_STATION_REQUEST);
        final long newStationId = 역_생성하고_아이디_반환(NEW_STATION_REQUEST);

        final long lineId = 노선_생성하고_아이디_반환(LINE_NINE_CREATE_REQUEST);

        노선에_최초의_역_2개_추가_요청(lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station1Id, 3
                ));

        final ExtractableResponse<Response> response = 노선에_역_1개_추가_요청(station1Id, newStationId, lineId, 1);

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }
}
