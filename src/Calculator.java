//��һ��
//���ߴ�
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Calculator {
	public static void main(String[] args) throws ExpressionException {

		Polynomial exp = new Polynomial();
		while (true) {

			Scanner sc = new Scanner(System.in);
			String userInput = sc.nextLine();
			long startTime = System.currentTimeMillis();// ��ȡ��ǰʱ��
			/// String input = "x*x*x*y*ui*9+3*x*y*z*x";
			String pDer = "\\s*\\!d\\/d\\s*";
			String pSim = "\\s*\\!simplify";
			String pass = "[^a-zA-Z0-9\\+\\-\\*\\^\\s]";
			/// �ж���������
			/// ���ʽ
			Pattern p1 = Pattern.compile(pass);
			Matcher m1 = p1.matcher(userInput);
			/// ����---��
			Pattern p2 = Pattern.compile(pDer);
			Matcher m2 = p2.matcher(userInput);
			/// ����---��ֵ
			Pattern p3 = Pattern.compile(pSim);
			Matcher m3 = p3.matcher(userInput);
			if (!m1.find()) {
				try {
					CreatExpression ce = new CreatExpression();
					try {
						userInput = ce.Create(userInput);
					} catch (ExpressionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					exp = new Polynomial(userInput);
					System.out.println(exp.printStringP());
					/// System.out.println(exp.simplify("x",
					/// 2).printStringP());
				} catch (ExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (m2.find()) {
				String prefix = m2.group();
				int m = prefix.length();
				String v = userInput.substring(m);
				String j = exp.DerivativeP(v).printStringP();
				/// �ж��󵼵ı����Ƿ��Ǳ��ʽ�г��ֹ���
				if (j.equals("0")) {
					System.out.println("Error, no variable");
				} else {
					System.out.println(exp.DerivativeP(v).printStringP());
				}
			} else if (m3.find()) {
				String pVar = "[a-zA-Z]+\\=\\-?\\d+";
				String pSimplify = "\\s*!simplify" + "((" + "\\s" + pVar + ")" + "+)";
				Pattern pS = Pattern.compile(pVar);
				Matcher mS = pS.matcher(userInput);
				int judge = 0; /// �ж�simplify֮���Ƿ���var����ȡָ�ı�־
				int count = 0; /// �ж���ֵ������ȷ��ѭ��
				int judge_pn = 1;/// �жϱ���ȡָ����
				Polynomial s = new Polynomial();
				while (mS.find()) {// !simplify x=1
					judge++;
					String sExp = mS.group();
					char[] sExp_Array = sExp.toCharArray();
					int flag = 0; /// ��ǵȺŵ�λ��
					while (true) {
						if (String.valueOf(sExp_Array[flag]).equals("=")) {
							break;
						} else {
							flag++;
						}
					}
					String var = sExp.substring(0, flag);
					if (!exp.judgeVariableExist(var)) {
						System.out.println("Format error");
						throw new ExpressionException("Format error");
					}
					String str = sExp.substring(flag + 1, flag + 2);
					if (str.equals("-")) {
						judge_pn = -1;
						flag++;
					}
					String valueString = sExp.substring(flag + 1);
					int value = Integer.valueOf(valueString);
					if (count == 0) {
						s = exp.simplify(var, value, judge_pn);
					} else {
						s = s.simplify(var, value, judge_pn);
					}
					count++;
				}
				if (judge == 0) {
					System.out.println(exp.printStringP());
				} else {
					System.out.println(s.printStringP());
				}
			} else {
				System.out.println("Format error");
				throw new ExpressionException("Format error");
			}

			long endTime = System.currentTimeMillis();
			System.out.println("����ʱ�䣺" + (endTime - startTime) + "ms");
		}
	}
}
