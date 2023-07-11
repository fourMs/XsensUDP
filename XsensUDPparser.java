import com.cycling74.max.*;

public class XsensUDPparser extends MaxObject
{

	private static final String[] INLET_ASSIST = new String[]{
		"Data received from XSens via sadam.udpreceiver"
	};
	private static final String[] OUTLET_ASSIST = new String[]{
		"outlet 1 help"
	};
	
	public XsensUDPparser(Atom[] args)
	{
		declareInlets(new int[]{DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL});
		
		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);

	}
    
	public void bang()
	{
	}
    
	public void inlet(int i)
	{
	}
    
	public void inlet(float f)
	{
	}
    
    
	public void list(byte[] buf)
	{
		
	/*	outlet(0, new StringBuilder().append("position ").append
			      (((X2) this).X2Geom1.p1[0]).append
			      (" ").append
			      (((X2) this).X2Geom1.p1[1]).append
			      (" ").append
			      (((X2) this).X2Geom1.p1[2]).toString());
	}
	

	public void unPack(byte[] buf)
	// Unpacks the Xsens datagram buffer, see Xsens Netstreamer documentation
	{*/
	// http://www.daniweb.com/code/snippet216874.html
		byte[] id = {buf[0], buf[1], buf[2], buf[3], buf[4], buf[5]};
		String ids = new String(id);
		byte[] b = {buf[6], buf[7], buf[8], buf[9]}; 	
		int frameNr = toInt(b);
	//	post("Recieved frame number : " + frameNr);
		int segs = (int) buf[11]; // Amounts of segments
		
		if (ids.substring(4,5).equals("01")) {post("type 1"); };
		
		// Unpack segments
		for (int i = 24; i < segs*28; i = i+28) // Start in position 24
		{ 
			byte[] bsid = {buf[i], buf[i+1], buf[i+2], buf[i+3]};
			int segId = toInt(bsid);
			byte[] pos = new byte[12]; // position buffer
		//	byte[] rot = new byte[12]; // orientation buffer (needs to be 16 if quaternion) 
			System.arraycopy(buf, i+4, pos, 0, 12);
		//	System.arraycopy(buf, i+16, rot, 0, 12);
			float[] posf = toFloatA(pos);
		//	float[] rotf = toFloatA(rot);
			String head = "/Xsens/"+segId+"/pos";
			outlet(0, new Atom[]{Atom.newAtom(head), Atom.newAtom(posf[0]),
					Atom.newAtom(posf[1]), Atom.newAtom(posf[2])});
		//	String head2 = "/Xsens/"+segId+"/rot";
		//	outlet(0, new Atom[]{Atom.newAtom(head2), Atom.newAtom(rotf[0]),
		//			Atom.newAtom(rotf[1]), Atom.newAtom(rotf[2])});
		}
	}

		
	public static int toInt(byte[] data) {
    if (data == null || data.length != 4) return 0x0;
    // ----------
    return (int)( // NOTE: type cast not necessary for int
            (0xff & data[0]) << 24  |
            (0xff & data[1]) << 16  |
            (0xff & data[2]) << 8   |
            (0xff & data[3]) << 0
            );
	}
	public static float toFloat(byte[] data) {
    if (data == null || data.length != 4) return 0x0;
    // ---------- simple:
    return Float.intBitsToFloat(toInt(data));
	}
	
	public static float[] toFloatA(byte[] data) {
    if (data == null || data.length % 4 != 0) return null;
    // ----------
    float[] flts = new float[data.length / 4];
    for (int i = 0; i < flts.length; i++) {
        flts[i] = toFloat( new byte[] {
            data[(i*4)],
            data[(i*4)+1],
            data[(i*4)+2],
            data[(i*4)+3],
        } );
    }
    return flts;
	}	
	
	
    
}




