package com.example;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;

public class GenModel implements ClassModelDecorator
{
	@Override
	public void decorate(ClassModelManager m)
	{
		// class SuperCar extends Car
		m.haveClass("SuperCar", m.haveClass("Car"), c -> {});

		System.out.println(">>>>>>>>>>>>>>> decorated! <<<<<<<<<<<<<<<");
	}
}
