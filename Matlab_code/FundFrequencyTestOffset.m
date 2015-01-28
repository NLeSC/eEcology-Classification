clc; close all, clear all
% prepare non-zero mean input signal with known frequency 
% offset
offset = 3
% duration [s]
T=1;
% sample rate [Hz] 
Fs = 20;
% samples
n = T*Fs;
%samples vector
t = 0 : 1/Fs : T  - 1/Fs;
%Frequency [Hz]
Fn = 3;
%Signal
input = sin(Fn*2*pi*t);
% add offset
input = input + offset;

figure; plot(t,input); title('Non-zero mean signal');

% remove the mean to get zero mean
m = mean(input);
input = input - m;

figure;plot(t,input); title('Zero mean signal');


%n = length(input);
n2 = n*2;

% Hamming window
%windowed_input =  hamming(n)'.*input

% % FFT
% output = fft(windowed_input);
% fft_otput = zeros(1,n2);
% fft_output(1:2:n2) = real(output);
% fft_output(2:2:n2) = imag(output);
% fft_output'

% periodogram
hper = spectrum.periodogram('hamming');
pd = psd(hper,input,'Fs', Fs, 'NFFT', n);
pdData = pd.Data;
pdFreqs = pd.Frequencies;

% exclude zero frequency component (DC)
[nonzeros,~] = find(pdFreqs>0);
pdData = pdData(nonzeros);
pdFreqs = pdFreqs(nonzeros);

% maximum amplitude and frequency at maximum amplitude
[maxAmp, maxI] = max(pdData)
argmaxAmp = pdFreqs(maxI)

