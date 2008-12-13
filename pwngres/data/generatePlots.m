function generatePlots(data)

    colors = 'rgmcb'; 

    for i=1:5,
        semilogy(data(:,i), colors(i))
        hold on
    end
    
    figure
    
    for i=1:4,
        error = abs(data(:,i) - data(:,5));
        plot(error, colors(i))
        hold on
    end

end