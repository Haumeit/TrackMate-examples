package plugin.trackmate.examples.detector;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImagePlusAdapter;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import fiji.plugin.trackmate.detection.DogDetector;

public class PreProcessingDoGDetector< T extends RealType< T > & NativeType< T >> extends DogDetector< T >
{

	/*
	 * CONSTANTS
	 */

	public final static String BASE_ERROR_MESSAGE = "PreprocessingDoGDetector: ";

	/*
	 * CONSTRUCTOR
	 */

	public PreProcessingDoGDetector( final RandomAccessible< T > img, final Interval interval, final double[] calibration, final double radius, final double threshold, final boolean doSubPixelLocalization, final boolean doMedianFilter )
	{
		super( img, interval, calibration, radius, threshold, doSubPixelLocalization, doMedianFilter );
		this.baseErrorMessage = BASE_ERROR_MESSAGE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean process()
	{
		final RandomAccessible<T> saved = img;
	
		// pre-process
		final RandomAccessibleInterval<T> interval = Views.interval( img, this.interval );
		final ImagePlus imp = ImageJFunctions.wrap( interval, img.toString() );
		IJ.run( imp, "Subtract Background...", "rolling=50 stack" );
		img = ImagePlusAdapter.wrapReal( imp );

		// process
		boolean result = super.process();

		img = saved;
		return result;
	}

}
