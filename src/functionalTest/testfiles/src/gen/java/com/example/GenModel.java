package com.example;

import org.fulib.classmodel.Clazz;
import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;

public class GenModel implements ClassModelDecorator
{
	@Override
	public void decorate(ClassModelManager m)
	{
		// class SuperCar extends Car

		Clazz car = m.haveClass("Car");
		Clazz superCar = m.haveClass("SuperCar");
		superCar.setSuperClass(car);

		System.out.println(">>>>>>>>>>>>>>> decorated! <<<<<<<<<<<<<<<");
	}
}
