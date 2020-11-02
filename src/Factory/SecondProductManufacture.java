package Factory;

public class SecondProductManufacture implements Manufacture {
    @Override
    public Product createProduct() {
        return new SecondProduct();
    }
}
