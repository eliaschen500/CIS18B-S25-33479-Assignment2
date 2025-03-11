import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

// Abstract class representing a generic library item
abstract class Item {
    private String title;
    private int publicationYear;

    public Item(String title, int publicationYear) {
        this.title = title;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    // Abstract method to display item information
    public abstract void displayInfo();
}

// Interface defining borrowable items
interface IBorrowable {
    void borrowItem(String borrower);
    void returnItem();
    boolean isBorrowed();
}

// Book class extends Item and implements IBorrowable
class Book extends Item implements IBorrowable {
    private String author;
    private String ISBN;
    private String borrower;

    public Book(String title, int publicationYear, String author, String ISBN) {
        super(title, publicationYear);
        this.author = author;
        this.ISBN = ISBN;
        this.borrower = null;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    // Allows a user to borrow the book if it's not already borrowed
    @Override
    public void borrowItem(String borrower) {
        if (this.borrower == null) {
            this.borrower = borrower;
            System.out.println(getTitle() + " is now borrowed by " + borrower + ". Enjoy reading!");
        } else {
            System.out.println("Oops! " + getTitle() + " is already with " + this.borrower + ". Maybe check back later?");
        }
    }

    // Allows a user to return the book
    @Override
    public void returnItem() {
        if (this.borrower != null) {
            System.out.println(getTitle() + " has been returned by " + this.borrower + ". Thanks for bringing it back!");
            this.borrower = null;
        } else {
            System.out.println(getTitle() + " wasn't even borrowed. Maybe you got confused?");
        }
    }

    @Override
    public boolean isBorrowed() {
        return borrower != null;
    }

    @Override
    public void displayInfo() {
        System.out.println("Book: " + getTitle() + " (" + getPublicationYear() + ") by " + author);
    }
}

// Magazine class extending Item but does not implement IBorrowable
class Magazine extends Item {
    private int issueNumber;

    public Magazine(String title, int publicationYear, int issueNumber) {
        super(title, publicationYear);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public void displayInfo() {
        System.out.println("Magazine: " + getTitle() + " (Issue " + issueNumber + ", " + getPublicationYear() + ")");
    }
}

// Singleton class for managing library items
class Library {
    private static Library instance;
    private List<Item> items;

    private Library() {
        items = new ArrayList<>();
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    // Lists available items, skipping borrowed books
    public void listAvailableItems() {
        for (Item item : items) {
            if (item instanceof Book && ((Book) item).isBorrowed()) {
                continue;
            }
            item.displayInfo();
        }
    }

    // Finds an item by its title
    public Item findItemByTitle(String title) {
        for (Item item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                return item;
            }
        }
        return null;
    }
}

// Factory Pattern for creating different types of library items
class LibraryItemFactory {
    public static Item createItem(String type, String title, int publicationYear, String extraData) {
        if (type.equalsIgnoreCase("book")) {
            return new Book(title, publicationYear, extraData, "Unknown ISBN");
        } else if (type.equalsIgnoreCase("magazine")) {
            return new Magazine(title, publicationYear, Integer.parseInt(extraData));
        } else {
            throw new IllegalArgumentException("Invalid item type. Try 'book' or 'magazine'.");
        }
    }
}

// Main class to run the library system
public class LibraryTest {
    public static void main(String[] args) {
        Library library = Library.getInstance();
        library.addItem(new Book("To Kill a Mockingbird", 1960, "Harper Lee", "123456789"));
        library.addItem(new Book("Pride and Prejudice", 1813, "Jane Austen", "987654321"));
        library.addItem(new Magazine("Time", 2023, 5));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary System: Choose an option:");
            System.out.println("1. List available items");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    library.listAvailableItems();
                    break;
                case 2:
                    System.out.print("Enter the book title to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    Item item = library.findItemByTitle(borrowTitle);
                    if (item instanceof Book) {
                        System.out.print("Enter your name: ");
                        String name = scanner.nextLine();
                        ((Book) item).borrowItem(name);
                    } else {
                        System.out.println("Book not found or not borrowable.");
                    }
                    break;
                case 3:
                    System.out.print("Enter the book title to return: ");
                    String returnTitle = scanner.nextLine();
                    Item returnItem = library.findItemByTitle(returnTitle);
                    if (returnItem instanceof Book) {
                        ((Book) returnItem).returnItem();
                    } else {
                        System.out.println("Book not found or not borrowable.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting the Library System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
