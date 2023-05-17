package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionAdapter extends DefaultWeightedEdge {

    public SectionAdapter() {
    }

    public Section toSection() {
        return new Section(getSource(), getTarget(), weight());
    }

    @Override
    protected Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    protected Station getTarget() {
        return (Station) super.getTarget();
    }

    public int weight() {
        return (int) super.getWeight();
    }
}
