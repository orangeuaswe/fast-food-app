package com.example.project_5.model;

public class Combo extends MenuItem
{
	private Sandwich earlOfSandwich;
	private Beverage bigGulp;
	private Side side;

	public Combo()
	{
		this.quantity = 1;
		this.earlOfSandwich = new Sandwich();
		this.bigGulp = new Beverage(Size.MEDIUM, Flavor.COLA);
		this.side = new Side(Size.SMALL, SideOption.CHIPS);
	}

	public Combo(Sandwich sandwich, Beverage drink, Side side)
	{
		this.quantity = 1;
		this.earlOfSandwich = sandwich;
		this.bigGulp = drink;
		this.side = side;
		bigGulp.setSize(Size.MEDIUM);
		side.setSize(Size.SMALL);
	}

	public Sandwich getSandwich()
	{
		return earlOfSandwich;
	}

	public Beverage getDrink()
	{
		return bigGulp;
	}

	public void setDrink(Beverage drink)
	{
		this.bigGulp = drink;
		bigGulp.setSize(Size.MEDIUM);
	}

	public Side getSide()
	{
		return side;
	}

	public void setSide(Side side)
	{
		this.side = side;
		side.setSize(Size.SMALL);
	}

	@Override
	public double cost()
	{
		double base = earlOfSandwich.cost() + bigGulp.cost() + side.cost();
		base += 2.00;  // combo surcharge
		return base * quantity;
	}


	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();

		// Strip off the sandwich's embedded price (split on space-then-dollar)
		String sandwichDesc = earlOfSandwich.toString().split(" \\$")[0];

		string.append("Combo: ").append(sandwichDesc);
		string.append(", ").append(side.getSide());
		string.append(", ").append(bigGulp.getFlavor());

		if (quantity > 1)
		{
			string.append(" (").append(quantity).append(")");
		}

		string.append(" $").append(String.format("%.2f", cost()));

		return string.toString();
	}
}
