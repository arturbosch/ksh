package io.gitlab.arturbosch.ksh.processor

import io.gitlab.arturbosch.ksh.api.ShellClass
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


/**
 * @author Artur Bosch
 */
@SupportedOptions("debug")
@SupportedAnnotationTypes("io.gitlab.arturbosch.ksh.api.ShellCommand")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ShellClassProcessor : AbstractProcessor() {

	override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
		if (roundEnv.processingOver()) {
			generateConfigFiles()
		} else {
			processAnnotations(annotations, roundEnv)
		}
		return true
	}

	private val names = mutableSetOf<String>()

	private fun processAnnotations(annotations: Set<TypeElement>, roundEnv: RoundEnvironment) {
		val shellClasses = roundEnv.getElementsAnnotatedWith(ShellClass::class.java)

		log(shellClasses.toString())
		log(annotations.toString())

		for (klass in shellClasses) {
			if (klass is TypeElement) {
//				val shellAnnotation = klass.annotationMirrors
//						.find { it.annotationType.enclosingType.getAnnotation(ShellClass::class.java) != null }
//				shellAnnotation?.let {
//					it.
//				}
				val qualifiedName = klass.qualifiedName.toString()
				log(qualifiedName)
				names.add(qualifiedName)
			}
		}

	}

	private fun generateConfigFiles() {
//		val filer = processingEnv.filer
//		val allServices = mutableSetOf<String>()
//		try {
//			// would like to be able to print the full path
//			// before we attempt to get the resource in case the behavior
//			// of filer.getResource does change to match the spec, but there's
//			// no good way to resolve CLASS_OUTPUT without first getting a resource.
//			val existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile)
//			log("Looking for existing resource file at " + existingFile.toUri())
//			val oldServices = ServicesFiles.readServiceFile(existingFile.openInputStream())
//			log("Existing service entries: " + oldServices)
//			allServices.addAll(oldServices)
//		} catch (e: IOException) {
//			// According to the javadoc, Filer.getResource throws an exception
//			// if the file doesn't already exist.  In practice this doesn't
//			// appear to be the case.  Filer.getResource will happily return a
//			// FileObject that refers to a non-existent file but will throw
//			// IOException if you try to open an input stream for it.
//			log("Resource file did not already exist.")
//		}

	}

	private val tempFile = File("/home/artur/test/ksh.txt")

	private fun log(content: String) {
		println(content)
		processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, content)
		tempFile.appendText(content)
	}
}