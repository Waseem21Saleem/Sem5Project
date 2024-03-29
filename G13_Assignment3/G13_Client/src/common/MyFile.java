package common;

import java.io.Serializable;

/**
 * MyFile class represents a serializable object for storing file data.
 * It includes attributes such as file name, size, byte array, and description.
 * 
 * This class allows initialization of a byte array, setting and getting file attributes, and serializing/deserializing file data.

 */
public class MyFile implements Serializable {
	
	 /** Description of the file. */
    private String description;

    /** Name of the file. */
    private String fileName;

    /** Size of the file. */
    private int size;

    /** Byte array to store file data. */
    public byte[] mybytearray;
	
	
	  /**
     * Initializes the byte array with the specified size.
     * 
     * @param size The size of the byte array.
     */
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	   /**
     * Constructs a MyFile object with the given file name.
     * 
     * @param fileName The name of the file.
     */
	public MyFile( String fileName) {
		this.fileName = fileName;
	}
	
	
	  /**
     * Retrieves the file name.
     * 
     * @return The name of the file.
     */
	public String getFileName() {
		return fileName;
	}

	   /**
     * Sets the file name.
     * 
     * @param fileName The name of the file.
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
    /**
     * Retrieves the size of the file.
     * 
     * @return The size of the file.
     */
	public int getSize() {
		return size;
	}

	  /**
     * Sets the size of the file.
     * 
     * @param size The size of the file.
     */
	public void setSize(int size) {
		this.size = size;
	}

	   /**
     * Retrieves the byte array containing file data.
     * 
     * @return The byte array containing file data.
     */
	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	   /**
     * Retrieves the byte at the specified index from the byte array.
     * 
     * @param i The index of the byte to retrieve.
     * @return The byte at the specified index.
     */
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	 /**
     * Sets the byte array with the provided data.
     * 
     * @param mybytearray The byte array containing file data.
     */
	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	  /**
     * Retrieves the description of the file.
     * 
     * @return The description of the file.
     */
	public String getDescription() {
		return description;
	}

	/**
     * Sets the description of the file.
     * 
     * @param description The description of the file.
     */
	public void setDescription(String description) {
		this.description = description;
	}	
}

