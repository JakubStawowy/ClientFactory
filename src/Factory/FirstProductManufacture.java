package Factory;

public class FirstProductManufacture implements Manufacture {
    @Override
    public Product createProduct() {
        return new FirstProduct();
    }
}
