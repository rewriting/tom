package tom.library.theory.internal;

public class Statistic {
	private int successCount;
	private int assumptionViolationCount;
	private int failureCount;
	private int badInputCount;

	public void increaseSuccessCount() {
		successCount++;
	}
	
	public void increaseAssumptionViolationCount() {
		assumptionViolationCount++;
	}
	
	public void increaseFailureCount() {
		failureCount++;
	}
	
	public void increaseBadInputCount() {
		badInputCount++;
	}
	
	public String generateStatistic() {
		StringBuilder sb = new StringBuilder();
		/*
		sb.append(String.format("Statistic: "
				+ "\n%s test data are generated:"
				+ "\n%s data are tested"
				+ "\n%s data are not tested due to assumption violation"
				+ "\n%s data are not tested due to marked as bad input", 
				calculateTotalGeneratedData(),
				calculateTotalTestedData(),
				assumptionViolationCount,
				badInputCount));
		*/
		sb.append(String.format("Statistic: "
				+ "\n%s test data are generated:"
				+ " (tested/assumption violation/bad input): %s/%s/%s", 
				calculateTotalGeneratedData(),
				calculateTotalTestedData(),
				assumptionViolationCount,
				badInputCount));
		
		return sb.toString();
	}
	
	private int calculateTotalGeneratedData() {
		int total = calculateTotalTestedData();
		total += assumptionViolationCount;
		total += badInputCount;
		return total;
	}
	
	private int calculateTotalTestedData() {
		int total = successCount;
		total += failureCount;
		return total;
	}
	
	public int getSuccessCount() {
		return successCount;
	}

	public int getAssumptionViolationCount() {
		return assumptionViolationCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public int getBadInputCount() {
		return badInputCount;
	}
}
