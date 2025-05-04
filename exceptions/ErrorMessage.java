package co.edu.uniandes.dse.carmotor.exceptions;

public final class ErrorMessage {
  public static final String ASSESSOR_NOT_FOUND = "The assessor with the given ID was not found";
  public static final String BANKING_NOT_FOUND = "The bank with the given ID was not found";
  public static final String INSURANCE_POLICY_NOT_FOUND = "The insurance policy with the given ID was not found";
  public static final String LOCATION_NOT_FOUND = "The location with the given ID was not found";
  public static final String MAINTENANCE_HISTORY_NOT_FOUND = "The maintenance history with the given ID was not found";
  public static final String PHOTO_NOT_FOUND = "The photo with the given ID was not found";
  public static final String TEST_DRIVE_NOT_FOUND = "The test drive with the given ID was not found";
  public static final String USER_NOT_FOUND = "The user with the given ID was not found";
  public static final String VEHICLE_NOT_FOUND = "The vehicle with the given ID was not found";  
  
  private ErrorMessage() {
	  throw new IllegalStateException("Utility class");
	}
}