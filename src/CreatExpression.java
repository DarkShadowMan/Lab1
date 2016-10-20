//µÚËÄ´Î
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CreatExpression {
	// public Polynomial root;
	public CreatExpression() {

	}

	public static boolean checkExpression(String expression) {
		// boolean flag = true;
		Pattern p1 = Pattern.compile("[^a-zA-Z0-9\\+\\-\\*\\^\\(\\)\\s]");
		Matcher m1 = p1.matcher(expression);
		if (m1.find()) {
			return false;
		}
		int LeftParNum = 0;
		char[] expCharArr = expression.toCharArray();
		for (int i = 0; i < expression.length(); i++) {
			if (expCharArr[i] == '(') {
				LeftParNum++;
			} else if (expCharArr[i] == ')') {
				LeftParNum--;
			}

			if (LeftParNum < 0) {
				return false;
			}
		}
		if (LeftParNum != 0) {
			return false;
		}
		return true;
	}

	public static String reducePower(String expression) throws ExpressionException {
		Stack<String> expNoPower = new Stack<String>();
		boolean isFactor = false;
		while (expression.length() > 0) {

			// number
			String number = "(\\d+)";
			Pattern p = Pattern.compile(number);
			Matcher m = p.matcher(expression);

			if (m.find() && m.start() == 0) {
				String findNum = m.group();
				expression = expression.substring(findNum.length(), expression.length());
				if (isFactor) {
					expNoPower.push("*");
				}
				expNoPower.push(findNum);
				isFactor = true;
				continue;
			}

			// var
			String var = "([a-zA-Z]+)";
			Pattern p1 = Pattern.compile(var);
			Matcher m1 = p1.matcher(expression);
			if (m1.find() && m1.start() == 0) {
				String findNum = m1.group();
				expression = expression.substring(findNum.length(), expression.length());
				if (isFactor) {
					expNoPower.push("*");
				}
				expNoPower.push(findNum);
				isFactor = true;
				continue;
			}

			// operator but not power
			String operator = "[\\+\\-\\*\\(\\)]";
			Pattern p2 = Pattern.compile(operator);
			Matcher m2 = p2.matcher(expression);
			if (m2.find() && m2.start() == 0) {
				String findNum = m2.group();
				expression = expression.substring(findNum.length(), expression.length());
				if (isFactor && findNum.compareTo("(") == 0) {
					expNoPower.push("*");
				} else if (isFactor == false) {
					System.out.println("Format Error");
					throw new ExpressionException("Format Error");
				}
				isFactor = false;
				expNoPower.push(findNum);
				continue;
			}

			// \\s
			String empty = "([\\s]+)";
			Pattern p3 = Pattern.compile(empty);
			Matcher m3 = p3.matcher(expression);
			if (m3.find() && m3.start() == 0) {
				String findNum = m3.group();
				expression = expression.substring(findNum.length(), expression.length());
				// treatedLetter+=findNum.length();
				continue;
			}

			// ^power
			String power = "([\\^])";
			Pattern p4 = Pattern.compile(power);
			Matcher m4 = p4.matcher(expression);

			if (m4.find() && m4.start() == 0) {
				String findNum = m4.group();
				expression = expression.substring(findNum.length(), expression.length());

				String numberPower = "(\\d+)";
				Pattern p0 = Pattern.compile(numberPower);
				Matcher m0 = p0.matcher(expression);

				if (!m0.find()) {
					throw new ExpressionException("Power is not a Positive Integer");
				}
				findNum = m0.group();
				int powern = Integer.parseInt(findNum);

				expression = expression.substring(findNum.length(), expression.length());

				String exp = new String();
				String pop = expNoPower.pop();

				if (pop.compareTo(")") == 0) {

					while (pop.compareTo("(") != 0) {
						exp = pop + exp;
						pop = expNoPower.pop();
					}
					exp = "(" + exp;
					expNoPower.push(exp);
					for (int j = 0; j < powern - 1; j++) {
						expNoPower.push("*" + exp);
					}
				} else {
					expNoPower.push(pop);
					for (int j = 0; j < powern - 1; j++) {
						expNoPower.push("*" + pop);
					}
				}
				isFactor = true;
				continue;
			}
		}

		String result = new String();// expNoPower
		while (!expNoPower.empty()) {
			result = expNoPower.pop() + result;
		}

		return result;
	}

	public static String Create(String expression) throws ExpressionException {
		String s = null;
		if (checkExpression(expression) == true) {
			try {
				s = reducePower(expression);
			} catch (ExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new ExpressionException("Format error");
		}
		return s;
	}

}