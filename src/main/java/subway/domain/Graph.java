package subway.domain;

import java.util.Set;

public interface Graph {

    void addStation(Station station);

    Section addSection(Station upStation, Station downStation);

    void setSectionDistance(Section section, int distance);

    boolean containsStation(Station station);

    Set<Section> downStationsOf(Station station);

    Set<Section> upStationsOf(Station station);

    Set<Station> stationSet();

    Station getUpStation(Section edge);

    Station getDownStation(Section edge);

    double getSectionDistance(Section edge);

    Section getSection(Station upStation, Station downStation);

    void removeSection(Station upStation, Station downStation);

    void removeSection(Section edge);

    Set<Section> sectionsOf(Station station);

    void removeStation(Station station);

    void removeAllSections(Set<Section> edges);

    boolean isTerminal(Direction direction, Station station);
}
