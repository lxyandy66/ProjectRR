package ctrl.base;

public class CtrlComponent {
	private int id;
	private String name;
	private ComponentType type;
	
	private int inletCount;
	private int outletCount;
	
	public CtrlComponent(int id, String name, ComponentType type, int inletCount, int outletCount) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.inletCount = inletCount;
		this.outletCount = outletCount;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public ComponentType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ComponentType type) {
		this.type = type;
	}

	/**
	 * @return the inletCount
	 */
	public int getInletCount() {
		return inletCount;
	}

	/**
	 * @param inletCount the inletCount to set
	 */
	public void setInletCount(int inletCount) {
		this.inletCount = inletCount;
	}

	/**
	 * @return the outletCount
	 */
	public int getOutletCount() {
		return outletCount;
	}

	/**
	 * @param outletCount the outletCount to set
	 */
	public void setOutletCount(int outletCount) {
		this.outletCount = outletCount;
	}
	
	
}
