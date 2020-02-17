package significance.distribution.util;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/* Score Distribution
 * SD = {(si,fi), ... , (sn,fn)} 
 * 
 * Set of pairs where each s is a score and f is the frequency of that score.
 * A integer value is calculated with the score (index). The frequency of that score is stored in scoreCount[index].
 * */


public class ScoreDistribution
{
  private double[] scoreCount; //scoreCount: 
  private double[] pvalues;
  protected boolean didComputePvalues = false;
  private int roundingFactor;
  private double maxScore;
  private double numRandomizations;

  /* Constructor: maxScore = max IC of a target set (term set)
   * 	          roundingFactor
   * */
  public ScoreDistribution(double maxScore, int roundingFactor)
  {
    this.maxScore = maxScore;
    this.roundingFactor = roundingFactor;
    
    int maxIndex = getIndexFromScore(maxScore) + 1;
    this.scoreCount = new double[maxIndex];
  }

  /** 
   * Get integer index from a score value.
   */
  private int getIndexFromScore(double score)
  {
    double roundedScore = Math.rint(score * this.roundingFactor) / this.roundingFactor;

    int index = (int)(roundedScore * this.roundingFactor);

    return index;
  }
  
  /**
   * Add a new score. Calculate the integer index with the score and increment the frequency.
   * @param score
   */
  public void addScore(double score)
  {
    int index = getIndexFromScore(score);
    if (index >= this.scoreCount.length) {
      throw new RuntimeException("Cannot put " + score + " to index: " + index + " (max: " + this.scoreCount.length + ")");
    }
    this.scoreCount[index] += 1.0D;
  }
  /**
   * Add a score several times.
   * @param score
   * @param times
   */

  public void addScore(double score, double times)
  {
    int index = getIndexFromScore(score);
    this.scoreCount[index] += times;
  }

  /**
   * The maximum score is the greater position which value distinct from 0 in the scoreCount array
   * @return max score
   */
  public double getMaxScore()
  {
    for (int i = this.scoreCount.length - 1; i > 0; i--)
    {
      if (this.scoreCount[i] > 0.0D) {
        return i / this.roundingFactor;
      }
    }
    return -1.0D;
  }
  
  /**
   * Get Score Distribution. SD = {(s1,f1),...,(sn,fn)}
   * @return score2count
   */

  public HashMap<Double, Double> getDistribution()
  {
    HashMap<Double, Double> score2count = new HashMap<Double, Double>();

    for (int i = 0; i < this.scoreCount.length; i++) {
      if (this.scoreCount[i] != 0.0D) {
    	  double score = i / this.roundingFactor;
    	  double count = this.scoreCount[i];
    	  score2count.put(Double.valueOf(score), Double.valueOf(count));
      }
    }
    return score2count;
  }
  

  /**
   * Returns the score distribution as R list. 
   * 
   * @param q
   * @param name
   * @return R list
   */
  public String getAsRlist(int q, String name)
  {
    StringBuffer scoresVector = new StringBuffer("c(");
    StringBuffer countsVector = new StringBuffer("c(");

    for (int i = 0; i < this.scoreCount.length; i++)
    {
      if (this.scoreCount[i] == 0.0D) {
        continue;
      }
      double score = i / this.roundingFactor;
      scoresVector.append(score);
      double count = this.scoreCount[i];
      countsVector.append(count);

      scoresVector.append(",");
      countsVector.append(",");
    }
    
    scoresVector.replace(scoresVector.length() - 1, scoresVector.length(), "");
    countsVector.replace(countsVector.length() - 1, countsVector.length(), "");

    scoresVector.append(")");
    countsVector.append(")");

    StringBuffer listBuffer = new StringBuffer();
    listBuffer.append("list(q=" + q);
    listBuffer.append(",");
    listBuffer.append("name=\"" + name + "\"");
    listBuffer.append(",");
    listBuffer.append("scores=" + scoresVector.toString());
    listBuffer.append(",");
    listBuffer.append("counts=" + countsVector.toString());
    listBuffer.append(")");

    return listBuffer.toString();
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer("SCORE DISTRIBUTION:\n");

    for (int i = 0; i < this.scoreCount.length; i++)
    {
      if (this.scoreCount[i] == 0.0D) {
        continue;
      }
      double score = i / this.roundingFactor;
      double count = this.scoreCount[i];

      buffer.append(score + "\t-\t" + count + "\n");
    }

    return buffer.toString();
  }

  public void clear()
  {
    this.scoreCount = new double[this.scoreCount.length];
    this.didComputePvalues = false;
    this.pvalues = null;
  }

  public double getPvalue(double score)
  {
    int index = getIndexFromScore(score);
    if (!this.didComputePvalues) {
      computePvalues();
    }
    return this.pvalues[index];
  }

  protected void computePvalues()
  {
    this.pvalues = new double[this.scoreCount.length];
    double revCumulativeCount = 0.0D;
    for (int i = this.scoreCount.length - 1; i >= 0; i--)
    {
      double count = this.scoreCount[i];
      revCumulativeCount += count;
      this.pvalues[i] = revCumulativeCount;

      if (revCumulativeCount < 0.0D) {
        throw new RuntimeException("Overflow");
      }
    }
    double completeCount = revCumulativeCount;

    for (int i = 0; i < this.pvalues.length; i++)
    {
      this.pvalues[i] /= completeCount;
    }

    this.didComputePvalues = true;
  }

  public synchronized void writeDistribution(String objectId, BufferedWriter outWriter)
    throws IOException
  {
    if (!this.didComputePvalues) {
      computePvalues();
    }
    double sumAllCounts = 0.0D;

    for (double countForScore : this.scoreCount) {
      sumAllCounts += countForScore;
    }

    outWriter.write(">" + objectId + "_" + sumAllCounts + "_" + this.maxScore + "_" + this.roundingFactor + "\n");
    for (int i = 0; i < this.scoreCount.length; i++)
    {
      if (this.scoreCount[i] == 0.0D) {
        continue;
      }
      double score = i / this.roundingFactor;
      double pvalue = this.pvalues[i];

      outWriter.write(score + "-");
      outWriter.write(String.format("%.5e", new Object[] { Double.valueOf(pvalue) }));
      outWriter.write("\n");
    }

    outWriter.flush();
  }

  public void setParsedDistribution(ArrayList<Float> scores, ArrayList<Float> pvalues)
  {
    this.pvalues = new double[this.scoreCount.length];

    HashMap index2pvalue = new HashMap();
    int lastIndexWithScore = -1;
    for (int i = 0; i < scores.size(); i++)
    {
      float score = ((Float)scores.get(i)).floatValue();
      float pvalue = ((Float)pvalues.get(i)).floatValue();
      int index = getIndexFromScore(score);
      index2pvalue.put(Integer.valueOf(index), Float.valueOf(pvalue));
      lastIndexWithScore = index;
    }

    double lastPvalue = 1.0D;
    for (int i = 0; i < this.scoreCount.length; i++)
    {
      double pvalueToPut = -1.0D;
      if (index2pvalue.containsKey(Integer.valueOf(i))) {
        pvalueToPut = ((Float)index2pvalue.get(Integer.valueOf(i))).floatValue();
      }
      else {
        pvalueToPut = lastPvalue;
        if (i >= lastIndexWithScore)
          pvalueToPut = 1.0D / this.numRandomizations;
      }
      this.pvalues[i] = pvalueToPut;
      lastPvalue = pvalueToPut;
    }
    this.didComputePvalues = true;
  }

  public void setSamplingSteps(double numRandomizations)
  {
    this.numRandomizations = numRandomizations;
  }
}

/* Location:           /Users/Armando/Documents/Proyectos/Pheno2/Aplicaciones/ComputeDistributions/ComputeDistributions.jar
 * Qualified Name:     de.charite.significance.distribution.util.ScoreDistribution
 * JD-Core Version:    0.6.0
 */