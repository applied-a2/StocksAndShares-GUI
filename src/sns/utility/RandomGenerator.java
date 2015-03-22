package sns.utility;
import java.util.*;

public class RandomGenerator {

	private static Random random = new Random();
	private static ArrayList<Long> existedIds = new ArrayList<Long>();

	public static int randomInt(int range)
	{
		return random.nextInt(range);
	}
	
	public static Long randomId()
	{
		if(existedIds.size() == 0)
		{
			Long randomId = random.nextLong();
			existedIds.add(randomId);
			//System.out.println(existedIds.size() + " ids");
			return randomId;
		}
		
		boolean flag = true;
		Long id = null;
		while(flag) {
			Long randomId = random.nextLong();
			
			boolean unusable = false;
			for(Long existedId: existedIds) {
				if(existedId.equals(randomId)) {
					unusable = true;
				}
			}
			if(unusable == false) {
				id = randomId;
				existedIds.add(id);
				//System.out.println(existedIds.size() + " ids");
				flag = false;
			}
		}
		return id;
	}
}
