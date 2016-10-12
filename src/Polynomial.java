///kjvggiugoiugohohih
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// 多项式类
public class Polynomial {
	public TreeMap<Monomial, Integer> variableP;/// <单项式，(无用)>

	public Polynomial() {

	}

	public Polynomial(String userInput) throws ExpressionException {
		variableP = new TreeMap<Monomial, Integer>();
		String pFactor = "((\\d+\\^\\d+)|([a-zA-Z]+\\^\\d+)|(\\d+)|([a-zA-Z]+))";
		String pMonomial = "(" + pFactor + "(\\s*(\\*)?\\s*" + pFactor + ")*)";

		Pattern p = Pattern.compile(pMonomial);
		Matcher m = p.matcher(userInput);
		while (m.find()) {
			String s = m.group();
			Monomial c_m = new Monomial(s);

			int start = m.start();
			if (!(start == 0) && userInput.toCharArray()[start - 1] == '-') {
				c_m.coefficient *= -1;
			}
			/// 合并同类项
			if (this.variableP == null || !this.variableP.containsKey(c_m)) {
				this.variableP.put(c_m, c_m.coefficient);
			} else {
				int cof1 = c_m.coefficient;
				int cof2 = this.variableP.get(c_m);
				this.variableP.remove(c_m);
				c_m.coefficient = cof1 + cof2;
				this.variableP.put(c_m, c_m.coefficient);
			}

			// /// 判断该变量是否在前面的表达式里出现过
			// if (!this.variableM.containsKey(m4String)) {
			// this.numberOfVariable++;
			// this.variableM.put(m4String, 1);
			// } else {
			// int valueM = this.variableM.get(m4String);
			// this.variableM.remove(m4String);
			// this.variableM.put(m4String, ++valueM);
			// }
		}
	}

	public String printStringP() {
		String outExp = "";
		Iterator<Entry<Monomial, Integer>> it = this.variableP.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Monomial, Integer> m = (Entry<Monomial, Integer>) it.next();
			Monomial p = m.getKey();
			if (m.getValue() >= 0) {
				outExp = outExp + "+" + p.printStringM();
			} else {
				outExp = outExp + "-" + p.printStringM();
			}
		}
		if (outExp.substring(0, 1).equals("+")) {
			outExp = outExp.substring(1);
		}
		return outExp;
	}

	/// 多项式求导数
	public Polynomial DerivativeP(String v) {
		Polynomial der = new Polynomial();
		der.variableP = new TreeMap<Monomial, Integer>();
		@SuppressWarnings("rawtypes")
		Iterator it = this.variableP.keySet().iterator();
		while (it.hasNext()) {
			Monomial m = (Monomial) it.next();
			Monomial n = m.DerivativeM(v);
			der.variableP.put(n, n.coefficient);
		}
		return der;
	}

	/// 多项式带入求值
	public Polynomial simplify(String x, int v, int judge) {
		Polynomial p = new Polynomial();
		p.variableP = new TreeMap<Monomial, Integer>();
		Iterator<Monomial> it = this.variableP.keySet().iterator();
		while (it.hasNext()) {
			Monomial m = it.next();
			Monomial n = m.simplify(x, v, judge);
			if (p.variableP == null || !p.variableP.containsKey(n)) {
				p.variableP.put(n, n.coefficient);
			} else {
				int cof1 = n.coefficient;
				int cof2 = p.variableP.get(n);
				p.variableP.remove(n);
				n.coefficient = cof1 + cof2;
				p.variableP.put(n, n.coefficient);
			}
		}
		return p;
	}

	/// 判断多项式中是否有变量V
	public boolean judgeVariableExist(String v) {
		Iterator<Entry<Monomial, Integer>> it = this.variableP.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Monomial, Integer> m = (Entry<Monomial, Integer>) it.next();
			Monomial p = m.getKey();
			if (p.judgeVariableExist(v)) {
				return true;
			}
		}
		return false;
	}
}