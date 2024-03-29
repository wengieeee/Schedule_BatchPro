package org.generate;

import java.util.List;

public class RandomCreater {
	
	public double taskLengthRate = 0.5;//任务长度浮动比
//	public double bandWithRate = 0.25; //带宽浮动比	
	public double levelRate = 0.5;//每一层任务的浮动比
	
	
	
	/**
	 * @param id 处理器编号
	 * @param nodeList 处理器节点列表
	 * @param capacity 处理器的peak，能力，处理速度
	 * @param endTime  处理器运行结束时间
	 * 
	 * 初始化处理器时   创建其上的   初始随机节点信息
	 */
	public void randomCreateNodes(int id,List<TaskNode> nodeList,int capacity,int endTime){
		int capacityLength = (int) (capacity*endTime); //处理器在100%利用率情况下，能处理的数据总长度
		int nodeNum = 0; //task任务节点编号
		int totalLength = 0;//任务长度	
		
		while(totalLength < capacityLength){
			nodeNum++;//从1开始
			
			/**
			 * 各个任务的长度取值范围【0.5*子任务平均长度，1.5*子任务平均长度】
			 */
			int taskLength = random((int)(BuildParameters.taskAverageLength*(1-taskLengthRate)), (int)(BuildParameters.taskAverageLength*(1+taskLengthRate)));//生成任务长度	
			
			TaskNode taskNode;
			if(taskLength+totalLength > capacityLength)//如果最新任务的长度 超过了处理器总任务长度
				taskLength = capacityLength -totalLength;//处理器总任务长度-当前总长度，这为最后一个任务的长度
			
			//任务编号：处理器编号_自我编号
			//创建新任务构造参数（任务id，任务长度，执行时间，结束时间）
			taskNode = new TaskNode(Integer.toString(id)+"_"+Integer.toString(nodeNum),taskLength,(totalLength/capacity),(taskLength+totalLength)/capacity);	
			nodeList.add(taskNode);
			totalLength+=taskLength;
		}			
	}
	
	
	  /**
	 * @param bandWidth 带宽数组
	 * @param size 数组大小
	 */
	/*public void randomBandWidths(double[][] bandWidth,int size){  		
		   for(int i=0;i<size;i++)
			   for(int j=0;j<=i;j++)
			   {
				 if(i==j)
					 bandWidth[i][j] = -1.00;
				 else
				 { 
					 bandWidth[i][j] = BandWidth.baseRate*(1+random()*bandWithRate);
					 bandWidth[j][i] = bandWidth[i][j];				 
				 }
		         
			   }
	  }	*/
	
	/**
	 * @param lastDagtime 上一个Dag提交的时间
	 * @param startTime 当前Dag开始执行的时间
	 * @return 随机生成的Dag的提交时间
	 */
	public int randomSubmitTime(int lastDagtime,int startTime){
		return random(lastDagtime, startTime);
	}
	
	
	
	/**
	 * @param maxlength 边上的差值
	 * @return 随机生成的数据传送时间
	 */
	public int randomTranferData(int maxlength)
	{
		if(maxlength == 0)
			return 0;
		else
			return random(1, maxlength);
	}
	
	/**
	 * 
	* @Title: randomDagSize 
	* @Description: 依据 平均DAG任务数 随机生成某DAG图的任务数   
	* 				范围【0.5*平均DAG任务数，1.5*平均DAG任务数】
	* @return int    
	* @throws
	 */
	public int randomDagSize(int dagAverageSize){
		return random((int)(dagAverageSize*0.5),(int)(dagAverageSize*1.5));
		
	}
	
	/**
	 * 
	* @Title: randomLevelNum 
	* @Description: 依据串行度生成整个DAG图的层数   
	* @return int    
	* @throws
	 */
	public int randomLevelNum(int dagSize,int levelFlag){
		int sqrt = (int)Math.sqrt(dagSize-2);
		if(levelFlag == 1)
			return random(1, sqrt);
		else if(levelFlag == 2)
			return random(sqrt,sqrt+3);
		else if(levelFlag == 3)
			return random(sqrt+3,dagSize-2);
		else
			return sqrt;		
	}
	
	/**
	 * 
	* @Title: randomLevelSizes 
	* @Description: 设置DAG图的第二层至倒数第二层中每一层的任务数目，随机设置   
	* @return void    
	* @throws
	 */
	
	public void randomLevelSizes(int[] dagLevel,int nodeNumber){
		//先给每层的任务数目都初始化为1
		for(int j = 0;j < dagLevel.length;j++)
			 dagLevel[j] = 1;
		int i = nodeNumber - dagLevel.length;
		
		//随机为每一层增加任务数
		 while(i > 0)
		 {
			 for(int j = 0;j < dagLevel.length;j++)
		
				  if(random(0, 1) == 1)
				  {
					  dagLevel[j]++;
					  i--;
					  if(i == 0)
						  break;
				  }	
			 if(i == 0)
				 break;
		 }
	}
	
	/**
	 * @return 产生[min,max]之间的随机数
	 */
	public int random(int min,int max){
		return (int)(min + Math.random()*(max-min+1));
	}
	

}

