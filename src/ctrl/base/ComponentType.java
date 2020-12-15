package ctrl.base;


public enum ComponentType {
	_NONE(-1),
	BROADCAST(0),
	CHILLER(1),
	COOLING_TOWER(2),
	CHILLER_PUMP(3),
	COOLING_PUMP(4);
	
	private final int code;
	
	
	private ComponentType(int code) {
		for (ComponentType type : values()) {
            if (type.code == code) {
            	this.code=code;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid CellType code: " + code);
	}
	
	public static boolean checkCodeValid(int code) {
		for (ComponentType type : values()) {
            if (type.code == code) {
                return true;
            }
        }
		return false;
	}
	
	
	
}
