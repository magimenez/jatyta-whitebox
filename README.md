## Synopsis

This is the implementation for whitebox testing support of Jatyta Crawljax Testing.

## Code Example

The goal of this library is to add microdata attributes (https://schema.org/) in the form elements of web applications to give them information about the kind of data to use for the submit values.

## Installation

This version only support Java web applications with Faces.

This is a maven project, execute mvn clean install to add the artifact in your  maven repository.

In your faces-config.xml add the follow configuration:

For faces 1.2 web applications:

	<render-kit>
		<renderer>
			<component-family>javax.faces.Form</component-family>
			<renderer-type>javax.faces.Form</renderer-type>
			<renderer-class>py.una.pol.jatyta.faces.renderer.renderkit.MicroDataFormRenderer</renderer-class>
		</renderer>
		<renderer>
			<component-family>javax.faces.Input</component-family>
			<renderer-type>javax.faces.Secret</renderer-type>
			<renderer-class>py.una.pol.jatyta.faces.renderer.renderkit.MicroDataSecretRenderer</renderer-class>
		</renderer>
		<renderer>
			<component-family>javax.faces.Input</component-family>
			<renderer-type>javax.faces.Textarea</renderer-type>
			<renderer-class>py.una.pol.jatyta.faces.renderer.renderkit.MicroDataTextAreaRenderer</renderer-class>
		</renderer>
		<renderer>
			<component-family>javax.faces.Input</component-family>
			<renderer-type>javax.faces.Text</renderer-type>
			<renderer-class>py.una.pol.jatyta.faces.renderer.renderkit.MicroDataTextRenderer</renderer-class>
		</renderer>
		<renderer>
			<component-family>javax.faces.SelectOne</component-family>
			<renderer-type>javax.faces.Radio</renderer-type>
			<renderer-class>py.una.pol.jatyta.faces.renderer.renderkit.MicroDataRadioRenderer</renderer-class>
		</renderer>
		<renderer>
			<component-family>javax.faces.SelectMany</component-family>
			<renderer-type>javax.faces.Checkbox</renderer-type>
			<renderer-class>py.una.pol.jatyta.faces.renderer.renderkit.MicroDataSelectManyRenderer</renderer-class>
		</renderer>
	</render-kit>

For faces 2.X web applications:

	<factory>
		<render-kit-factory>py.una.pol.jatyta.faces.renderer.MicroDataRenderKitFactory</render-kit-factory>
	</factory>

## API Reference

This version only support Java web applications, you need to add the follow annotation in your Java classes that represent the model of your application.

@JatytaAnnotations( itemscope="", itemtype="" )
For class definition, example:

@JatytaAnnotations( itemscope=true, itemtype="https://schema.org/Person" )
public class Person {
	...
}

This add the itemscope and itemtype attributes (and values) in the form asociated with the model class (Person).

For class attribute, example:

@JatytaAnnotations( itemprop="name" )
private String name;

This add the itemprop attribute in the form field associated with the name attribute of the class model. 

## License

This project is licensed under the "Apache License, Version 2.0".