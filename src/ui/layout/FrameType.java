package ui.layout;

import java.util.Set;

public enum FrameType {
    SINGLE(Set.of(Region.MAIN)),
    DOUBLE_VERTICAL(Set.of(Region.TOP, Region.BOTTOM)),
    DOUBLE_HORIZONTAL(Set.of(Region.LEFT, Region.RIGHT)),
    QUAD(Set.of(Region.TOP_LEFT, Region.TOP_RIGHT, Region.BOTTOM_LEFT, Region.BOTTOM_RIGHT));
    private final Set<Region> regions;

    FrameType(Set<Region> regions) {
        this.regions = regions;
    }

    public boolean supports(Region region) {
        return regions.contains(region);
    }

    public Set<Region> getRegions() {
        return regions;
    }
}
