package mytcp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Table {
	int tablenumber; //����from 1
	int num=0; //player����
	Map<Integer, Player> clientsMap=new HashMap<Integer,Player>();   //0=<key<8
	public boolean addplayer(Player player) {
		//num++;
		@SuppressWarnings("rawtypes")
		Set keySet=clientsMap.keySet();
		for(int i=0;i<8;i++){
			if(!keySet.contains(i)){
				clientsMap.put(i, player);
				num++;
				return true;
			}
		}
		return false;
	}
	public void deleteplayer(Player player){
		@SuppressWarnings("rawtypes")
		Set keySet=clientsMap.keySet();
		@SuppressWarnings("unchecked")
		Iterator<Integer> iterator=keySet.iterator();
		while (iterator.hasNext()) {
			Integer integer=iterator.next();
			if(clientsMap.get(integer)==player)
			{
				clientsMap.remove(integer);
			}
			
		}
	}
}

