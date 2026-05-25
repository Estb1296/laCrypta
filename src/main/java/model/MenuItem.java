package model;

public abstract class MenuItem implements Priceable{
   private final String name;
   protected double price;
    private final String description;
        protected MenuItem(String name, double price, String description) {
            this.name = name;
            this.price = price;
            this.description=description;
        }

        public String getName() {
            return name;
        }
      public double getPrice(){
            return price;
      }

    public String getDescription() {
        return description;
    }
    @Override
    public double calculatePrice() {
        return price;
    }
    @Override
    public void addToPrice(double amount) {
        this.price += amount;
    }
    @Override
    public void subtractFromPrice(double amount) {
        this.price -= amount;
    }
    public abstract String toReceiptLine();
    public abstract String getCategory();
}
