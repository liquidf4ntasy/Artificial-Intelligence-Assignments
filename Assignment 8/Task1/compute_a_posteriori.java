
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author liquidf4ntasy
 * UTA ID: 1001417727
 */
public class compute_a_posteriori {

	/**
	 * @param args
	 */
	final static double	ph1 = 0.1;
	final static double	ph2 = 0.2;
	final static double	ph3 = 0.4;
	final static double	ph4 = 0.2;
	final static double	ph5 = 0.1;

	public static void main(String[] args) throws IOException {
		int Lcount =0;
		int Ccount = 0;

		FileWriter fw = new FileWriter("result.txt",true);
    	//BufferedWriter writer give better performance
    	BufferedWriter bw = new BufferedWriter(fw);
		
		String ip="..";
		if( args.length > 1 ) 
	    {
	      System.out.println("Only One command-line argument (string consisting of Ls and Cs) allowed:\n"
	                         + "Usage: java compute_a_posteriori <string>\n");
	      exit_function( 0 );
	     }
		
		else if(args.length == 0)
		{
			bw.write("Observation Sequence Q: 0 \n");
			bw.write("Length of Q: 0 \n");
	    	bw.close();

			calc(0,0);

		}
		else
			{
			ip = args[0];
		    if ( Pattern.matches("[\\dLC]+", ip)   || args[0] == null )
		    {}
		    	else
		    	{
		    		System.out.println(" string consisting of Ls and Cs allowed:\n"
		                         + "Usage: java compute_a_posteriori <string>\n");
		    		
		    		exit_function( 0 );
		     }
		  }
		
		// count no of Ls and Cs
	    for (int i = 0; i < ip.length(); i++) 
	    {
		      if (ip.charAt(i) == 'C')
		        Ccount++;
		      else if(ip.charAt(i) == 'L')
		    	  Lcount++;
		 }		    
	    
	    
		bw.write("Observation Sequence Q: " + ip+ "\n");
		bw.write("Length of Q: " + args[0].length() + "\n\n");
    	bw.close();
		calc(Ccount,Lcount);
		
			}

		
	
	  private static void exit_function( int value )
	  {
	      System.out.println("Exiting from compute_a_posteriori.java!\n");
	      System.exit( value );
	  }
	  
	  private static void calc (int count_of_C , int count_of_L) throws IOException
	  {
		  			double likelihood_h1 = (Math.pow(1, count_of_C)) * (Math.pow(0.0, count_of_L));
		  			double likelihood_h2 = (Math.pow(0.75, count_of_C)) * (Math.pow(0.25, count_of_L));
		  			double likelihood_h3 = (Math.pow(0.5, count_of_C)) * (Math.pow(0.50, count_of_L));
		  			double likelihood_h4 = (Math.pow(0.25, count_of_C)) * (Math.pow(0.75, count_of_L));
		  			double likelihood_h5 = (Math.pow(0.00, count_of_C)) * (Math.pow(1, count_of_L));

		  			double posterior_h1 = ph1 * likelihood_h1;
		  			double posterior_h2 = ph2 * likelihood_h2;
		  			double posterior_h3 = ph3 * likelihood_h3;
		  			double posterior_h4 = ph4 * likelihood_h4;
		  			double posterior_h5 = ph5 * likelihood_h5;

		  			double norm = posterior_h1 + posterior_h2 + posterior_h3 + posterior_h4 + posterior_h5;
					posterior_h1 /= norm;
					posterior_h2 /= norm;
					posterior_h3 /= norm;
					posterior_h4 /= norm;
					posterior_h5 /= norm;

					double nextC = 1 * posterior_h1 + 0.75 * posterior_h2 + 0.5 * posterior_h3 + 0.25 * posterior_h4 + 0 * posterior_h5;
					double nextL = 0 * posterior_h1 + 0.25 * posterior_h2 + 0.5 * posterior_h3 + 0.75 * posterior_h4 + 1 * posterior_h5;
					/*
					System.out.format("P(h1 | Q) = %.4f \n",   posterior_h1);
					System.out.format("P(h2 | Q) = %.4f \n",  posterior_h2);
					System.out.format("P(h3 | Q) = %.4f \n",  posterior_h3);
					System.out.format("P(h4 | Q) = %.4f \n",  posterior_h4);
					System.out.format("P(h5 | Q) = %.4f \n\n",  posterior_h5);
					System.out.format("Probability that the next candy we pick will be C, given Q: %.4f\n",  nextC);
					System.out.format("Probability that the next candy we pick will be L, given Q: %.4f\n",  nextL);
					*/
					double p[] ={posterior_h1,posterior_h2,posterior_h3,posterior_h4,posterior_h5,nextC,nextL };
					
				    PrintWriter out = new PrintWriter(new FileWriter("result.txt", true), true);
				    boolean flag=true;
					for (int i=0;i<7;i++)
					{
        		    BigDecimal roundfinalPrice = new BigDecimal(p[i]).setScale(5,BigDecimal.ROUND_HALF_UP);
        		    p[i] = roundfinalPrice.doubleValue();
        		    if(i ==5)
        		    	flag=false;
        		    if(flag == true)
					out.println("P(h" + (i+1)+ " | Q) = " + p[i]);
					}
					
					out.println("\nProbability that the next candy we pick will be C, given Q:  " +  p[5]);
					out.println("Probability that the next candy we pick will be L, given Q:  " +  p[6]);
					out.close();
					
	  }
		
}