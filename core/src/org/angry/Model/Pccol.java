
package org.angry.Model;

/**
 * Created by MyBaby on 12/15/2015.
 */
public class Pccol implements ColInterface
{

	public static final Pccol instance = new Pccol();
	
	@Override
	public void handleCollision(Handel m, Body a, Body b)
	{
		Cpcol.instance.handleCollision(m, b, a);
		
		if ( m.contactCount > 0 )
		{
			m.normal.negi();
		}
	}

}
