package ontologizer.go;


import ontologizer.go.Prefix;
import ontologizer.go.TermID;

class Pool<T>
{
}

/**
 * 
 * 
 * @author Sebastian Bauer
 */
public class OntologyPool
{
	static private Pool<Prefix> prefixPool = new Pool();
	static private Pool<TermID> termidPool = new Pool();
}

