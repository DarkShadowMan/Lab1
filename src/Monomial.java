import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// 单项式类
public class Monomial implements Comparable<Monomial> {
	public int coefficient = 1;/// 系数
	public int totalPower = 0;/// 总幂次
	public int numberOfVariable = 0;/// 变量的个数
	public TreeMap<String, Integer> variableM;/// <变量，变量的幂>

	public Monomial() {

	}

	public Monomial(String expressionM) {
		/// String input = "35111*111*x*Y*sss";
		variableM = new TreeMap<String, Integer>();
		String pFactor = "((\\d+\\^\\d+)|([a-zA-Z]+\\^\\d+)|(\\d+)|([a-zA-Z]+))";
		Pattern p4 = Pattern.compile(pFactor);
		Matcher m4 = p4.matcher(expressionM);
		while (m4.find()) {
			/// System.out.println(m4.group());
			String judge = "[a-zA-Z]+";
			String m4String = m4.group();
			Pattern p = Pattern.compile(judge);
			Matcher m = p.matcher(m4String);
			/// 匹配成功这说明是变量
			if (m.find()) {
				this.totalPower++;
				/// 判断该变量是否在前面的表达式里出现过
				if (this.variableM == null || this.variableM.containsKey(m4String) == false) {
					this.numberOfVariable++;
					this.variableM.put(m4String, 1);
				} else {
					int valueM = this.variableM.get(m4String);
					this.variableM.remove(m4String);
					this.variableM.put(m4String, ++valueM);
				}
			}
			/// 匹配失败则说明是数量
			else {
				int num = Integer.parseInt(m4String);
				this.coefficient *= num;
			}
		}
	}

	public int compareTo(Monomial a) {
		int programExit = 0;
		if (this.totalPower > a.totalPower) {
			programExit = -1;
		} else if (this.totalPower < a.totalPower) {
			programExit = 1;
		} else {
			if (this.numberOfVariable == a.numberOfVariable) {
				@SuppressWarnings("rawtypes")
				Iterator itM = this.variableM.keySet().iterator();
				@SuppressWarnings("rawtypes")
				Iterator it_a = a.variableM.keySet().iterator();
				while (itM.hasNext() && it_a.hasNext()) {
					String S_M = (String) itM.next();
					String S_a = (String) it_a.next();
					if ((S_M.equals(S_a) == false)
							|| ((S_M.equals(S_a) == true) && (!this.variableM.get(S_M).equals(a.variableM.get(S_a))))) {
						if (S_M.compareTo(S_a) < 0) {
							programExit = -1;
						} else {
							programExit = 1;
						}
						break;
					} else {
						programExit = 0;
					}
				}
			} else {
				// @SuppressWarnings("rawtypes")
				Iterator<String> itM = this.variableM.keySet().iterator();
				// @SuppressWarnings("rawtypes")
				Iterator<String> it_a = a.variableM.keySet().iterator();
				int flag = 1; /// 判断while循环出口
				while (itM.hasNext() && it_a.hasNext()) {
					String S_M = (String) itM.next();
					String S_a = (String) it_a.next();
					if (S_M.compareTo(S_a) < 0) {
						programExit = -1;
						flag = 0;
						break;
					} else if (S_M.compareTo(S_a) > 0) {
						programExit = 1;
						flag = 0;
						break;
					}
				}
				if (flag == 1) {
					if (itM.hasNext() == false) {
						programExit = -1;
					} else {
						programExit = 1;
					}
				}
			}
		}
		return programExit;
	}

	public String printStringM() {
		String outExp = String.valueOf(Math.abs(this.coefficient));
		if (this.coefficient == 0) {
			outExp = "0";
		} else {
			Iterator<String> it = this.variableM.keySet().iterator();

			while (it.hasNext()) {
				String s = (String) it.next();
				if (this.variableM.get(s) > 1) {
					String added = s + "^" + String.valueOf(this.variableM.get(s));
					if (outExp.equals("1")) {
						outExp = added;
					} else {
						outExp = outExp + "*" + added;
					}
				} else {
					if (outExp.equals("1") | outExp.equals("-1")) {
						outExp = s;
					} else {
						outExp = outExp + "*" + s;
					}
				}
			}
		}
		return outExp;
	}

	/// 单项式求导数
	public Monomial DerivativeM(String v) {
		Monomial der = new Monomial();
		der.variableM = new TreeMap<String, Integer>();
		der.coefficient = this.coefficient;
		Iterator<String> it = this.variableM.keySet().iterator();
		int judge = 0;
		while (it.hasNext()) {
			String s = (String) it.next();
			if (s.equals(v)) {
				judge = 1;
				int index = this.variableM.get(s);
				if (index != 0) {
					der.coefficient *= index;
					if (index != 1) {
						der.variableM.put(s, --index);
					}
				}
			} else {
				int index = this.variableM.get(s);
				der.variableM.put(s, index);
			}
		}
		if (judge == 0) {
			der.variableM.clear();
			der.coefficient = 0;
			der.numberOfVariable = 0;
		}
		return der;
	}

	/// 单项式带入求值
	public Monomial simplify(String x, int v, int judge) {
		if (judge < 0) {
			v = -v;
		}
		Monomial m = new Monomial();
		m.variableM = new TreeMap<String, Integer>();
		m.coefficient = this.coefficient;
		m.numberOfVariable = this.numberOfVariable;
		m.totalPower = this.totalPower;
		Iterator<String> it = this.variableM.keySet().iterator();
		while (it.hasNext()) {
			String s = it.next();
			if (s.equals(x)) {
				int index = this.variableM.get(s);
				m.numberOfVariable = m.numberOfVariable - 1;
				m.totalPower -= index;
				int cof = (int) Math.pow(v, index);
				m.coefficient *= cof;
			} else {
				int index = this.variableM.get(s);
				m.variableM.put(s, index);
			}
		}
		return m;
	}

	/// 判断单项式中是否有变量V
	public boolean judgeVariableExist(String v) {
		Iterator<String> it = this.variableM.keySet().iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			if (s.equals(v)) {
				return true;
			}
		}
		return false;
	}
}