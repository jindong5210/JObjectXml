JObjectXml v1.1.3 Released
----------------------------------- 

### 1.介绍

  JObjectXml是一个开源免费的第三方JAVA组件，用JObjectXml可以在XML和JAVA Bean之间进行方便的转换。用户需要在JAVA BEAN的属性或者类上面使用注解来标识出该属性对应的XML文档中的节点即可。

### 2.开始

示例JavaBean

	@ClassProps(alias = "test-bean")
	public class TestBean extends TestSuperBean{
	
	@FieldProps
	private int intAttr;
	
	@FieldProps(type = ElementType.ELEMENT,alias = "list-element1")
	private List<TestGen> listElmt1;
	
	@FieldProps(type = ElementType.ELEMENTLIST,alias = "list-element2")
	private List<TestSuperBean> listElmt2;
	
	@FieldProps(alias = "xmlns1")
	private String strAttr;
	
	@FieldProps(type = ElementType.ELEMENT)
	private TestGen testGen;
	
	@FieldProps
	private Date dateAttr;
	
	@FieldProps(type = ElementType.ELEMENT)
	private String strElmt;
	
	@FieldProps(type = ElementType.ELEMENTLIST,alias = "str-list",strAlias = "string")
	private List<String> strList;
	
	@FieldProps(type = ElementType.ELEMENTLIST,alias = "str-list1",strAlias = "string1")
	private List<String> strList1;
	
	}

### 3.转换

JavaBean to Xml

	JOXWriter writer = new JOXWriter();
	TestBean tb = new TestBean();
	tb.setStrAttr("http://www.atjava.com/Nebula");
	tb.setStrElmt("sdfsdfsdfsdf");
	tb.setSuperStr("superString");
	tb.setDateAttr(new Date());
	
	writer.writeXml(new File("/Users/jin-dong/123.xml"), tb);

Xml to JavaBean

	JOXReader reader = new JOXReader();
	System.out.println(reader.readXml(new File("c:/123.xml"), TestBean.class));
	
	
