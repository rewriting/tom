package test.tom.library.theory;

import static org.junit.Assert.*;
import static tom.library.shrink.tools.VisitableTools.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import examples.adt.table.table.types.Val;
import examples.lists.alist.types.AList;
import examples.lists.alist.types.Elem;

public class VisitableToolsTest {

	@Test
	public void testSize() {
		AList term = AList.fromString("con(cs(3),empty())");
		assertThat(size(term), equalTo(6));
	}
	
	@Test
	public void testSize2() {
		AList term = AList.fromString("con(cs(3),con(cs(6), empty()))");
		assertThat(size(term), equalTo(14));
	}
	
	@Test
	public void testSizeElem() {
		Elem term = Elem.fromString("cs(3)");
		assertThat(size(term), equalTo(4));
	}

	@Test
	public void testSizeTerminalConstructor() {
		AList term = AList.fromString("empty");
		assertThat(size(term), equalTo(1));
	}
	
	@Test
	public void testIsValueInstanceOfInteger() {
		Elem term = Elem.fromString("cs(3)");
		assertThat(isValueInstanceOfInteger(term.getChildAt(0)), equalTo(true));
	}

	@Test
	public void testIsValueInstanceOfIntegerButString() {
		String value = "thevallueofthedayishello";
		Val term = Val.fromString("val(\""+value+"\")");
		assertThat(isValueInstanceOfInteger(term.getChildAt(0)), equalTo(false));
	}
	
	@Test
	public void testIsValueInstanceOfString() {
		String value = "thevallueofthedayishello";
		Val term = Val.fromString("val(\""+value+"\")");
		assertThat(isValueInstanceOfString(term.getChildAt(0)), equalTo(true));
	}

	@Test
	public void testIsValueInstanceOfStringButInteger() {
		Elem term = Elem.fromString("cs(3)");
		assertThat(isValueInstanceOfString(term.getChildAt(0)), equalTo(false));
	}
	
	@Test
	public void testGetValueFromTermString() {
		String value = "thevallueofthedayishello";
		Val term = Val.fromString("val(\""+value+"\")");
		String result = getValueFromTermString(term.getChildAt(0));
		assertThat(result, equalTo(value));
	}

	@Test
	public void testGetValueFromTermInteger() {
		Elem term = Elem.fromString("cs(3)");
		int result = getValueFromTermInteger(term.getChildAt(0));
		assertThat(result, equalTo(3));
	}

}
